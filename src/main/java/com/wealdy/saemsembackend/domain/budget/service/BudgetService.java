package com.wealdy.saemsembackend.domain.budget.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_CATEGORY;
import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_USER;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetRecommendProjection;
import com.wealdy.saemsembackend.domain.budget.service.dto.BudgetSummaryDto;
import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final int MAX_RATIO = 10;  // 기타에 포함될 최대 비율

    // 예산 설정
    @Transactional
    public void createBudget(LocalDate date, List<BudgetSummaryDto> getBudgetDtoList, String loginId) {
        getBudgetDtoList
            .forEach(getBudgetDto -> {
                User user = findUser(loginId);
                Category category = categoryRepository.findByName(getBudgetDto.getCategoryName())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY));
                Optional<Budget> findBudget = budgetRepository.findByDateAndCategoryAndUser(date, category, user);
                findBudget.ifPresentOrElse(
                    budget -> budget.updateBudget(getBudgetDto.getAmount()),
                    () -> createBudget(date, getBudgetDto, user, category)
                );
            });
    }

    private void createBudget(LocalDate date, BudgetSummaryDto getBudgetDto, User user, Category category) {
        Budget budget = Budget.createBudget(date, getBudgetDto.getAmount(), user, category);
        budgetRepository.save(budget);
    }

    // 예산 목록 조회
    @Transactional(readOnly = true)
    public List<GetBudgetDto> getBudgetList(LocalDate date, String loginId) {
        User user = findUser(loginId);
        return budgetRepository.findByDateAndUser(date, user).stream()
            .map(projection -> GetBudgetDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }

    /*
        예산 추천

        1. 유저 별 예산 총합을 조회합니다. / 카테고리 별 유저 들의 예산 목록을 조회합니다.
        2. 카테고리 별로 유저들이 설정한 예산 비율을 구합니다.
        3. 카테고리 별 예산의 평균 비율을 구합니다.
          - 평균 비율 구하고 반올림 하면 카테고리들의 비율 총합이 항상 100%로 유지됩니다.
        4. 평균 비율이 10% 이하인 카테고리는 기타 비율에 추가합니다.
        5. 비율에 맞는 추천 금액을 계산합니다.
          - 추천 금액은 소수점일 수 없으므로 반올림합니다.
        6. 오차는 (최대 1원) 기타 카테고리에 반영합니다.
          - 기타 카테고리가 예산 추천 리스트의 가장 마지막에 추가되므로..
     */
    @Transactional(readOnly = true)
    public List<GetBudgetDto> recommendBudget(long budgetTotal, LocalDate date, String loginId) {
        checkUser(loginId);

        List<GetBudgetDto> budgetDtoList = new ArrayList<>();
        Map<Long, Long> sumOfBudgetByUserMap = new HashMap<>();  // 유저별 예산 총합
        budgetRepository.getSumOfBudgetGroupByUser(date)
            .forEach(projection -> sumOfBudgetByUserMap.put(projection.getUser().getId(), projection.getSumOfBudget()));
        int userCnt = sumOfBudgetByUserMap.size();  // user 수

        long recommendAmountTotal = 0;  // 추천 예산의 총 합
        int countForCategory = 0;  // 현재 카테고리로 설정된 예산 개수 카운트
        long ratioSum = 0;  // 카테고리 별 예산 비율 합계
        long etcRatioSum = 0;  // 기타로 제공 될 카테고리 비율의 합

        // 카테고리 별 유저들의 예산 목록
        List<BudgetRecommendProjection> budgetList = budgetRepository.findByDate(date);
        for (BudgetRecommendProjection budget : budgetList) {
            countForCategory++;

            String categoryName = budget.getCategoryName();

            // 카테고리 별 유저들이 설정한 예산 비율
            long categoryAmount = budget.getAmount();
            long totalAmount = sumOfBudgetByUserMap.get(budget.getUserId());
            long ratio = categoryAmount * 100 / totalAmount;
            ratioSum += ratio;

            // 해당 카테고리의 예산이 아직 존재하면 계속 확인
            if (countForCategory != userCnt) {
                continue;
            }

            // 해당 카테고리의 예산을 모두 확인했다면
            // 평균 비율 및 추천 금액 계산
            long avgRatio = ratioSum / userCnt;
            long amount = budgetTotal * avgRatio / 100;
            long recommendAmount = Math.round(amount);

            // 10% 미만인 카테고리 또는 기타 카테고리
            if (avgRatio < MAX_RATIO || categoryName.equals("기타")) {
                etcRatioSum += avgRatio;  // 기타로 제공 될 카테고리 비율을 합하기
            } else {
                // 그 외 카테고리는 예산 추천 객체 생성
                budgetDtoList.add(GetBudgetDto.of(categoryName, recommendAmount));
                recommendAmountTotal += recommendAmount;
            }

            // 다음 카테고리 통계를 위한 초기화
            countForCategory = 0;
            ratioSum = 0;
        }

        // 기타 카테고리는 모든 카테고리의 통계 계산이 끝난 후 예산 추천 금액 계산 및 객체 생성
        long etcRecommendAmount = Math.round(budgetTotal * etcRatioSum / 100.0);
        // 추천 예산의 총 합에 기타 카테고리 합하기
        recommendAmountTotal += etcRecommendAmount;
        // 오차는 기타 카테고리에 반영
        etcRecommendAmount += budgetTotal - recommendAmountTotal;

        // 기타 카테고리 예산 추천 객체 생성
        budgetDtoList.add(GetBudgetDto.of("기타", etcRecommendAmount));

        return budgetDtoList;
    }

    private User findUser(String loginId) {
        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    private void checkUser(String loginId) {
        userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }
}
