package com.wealdy.saemsembackend.domain.spending.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_CATEGORY;
import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_SPENDING;
import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_USER;

import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetTotalProjection;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import com.wealdy.saemsembackend.domain.core.enums.SpendingMessage;
import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.exception.NotSetException;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import com.wealdy.saemsembackend.domain.spending.repository.SpendingRepository;
import com.wealdy.saemsembackend.domain.spending.repository.projection.SpendingRecommendProjection;
import com.wealdy.saemsembackend.domain.spending.repository.projection.SpendingTodayProjection;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingListDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingMonthlyByCategoryDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingRecommendByCategoryDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingRecommendDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingStatisticsDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingSummaryDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingTodayByCategoryDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingTodayDto;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpendingService {

    private final SpendingRepository spendingRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private static final long MIN_RECOMMEND_AMOUNT = 1000; // 최소 추천 금액
    private static final double SAVING_WELL_RATIO = 70.0; // 최소 추천 금액

    // 지출 생성
    @Transactional
    public Long createSpending(LocalDateTime date, long amount, String memo, boolean excludeTotal, String categoryName, String loginId) {
        User user = findUser(loginId);
        Category category = findCategory(categoryName);

        Spending spending = spendingRepository.save(Spending.createSpending(date, amount, memo, excludeTotal, user, category));
        return spending.getId();
    }

    // 지출 수정
    @Transactional
    public void updateSpending(
        Long spendingId, LocalDateTime date, long amount, String memo, String categoryName, String loginId
    ) {
        User user = findUser(loginId);
        Category category = findCategory(categoryName);

        Spending spending = getSpending(spendingId, user);
        spending.updateSpending(date, amount, memo, category);
    }

    // 지출 합계 제외 수정
    @Transactional
    public void updateExclude(Long spendingId, boolean excludeTotal, String loginId) {
        User user = findUser(loginId);

        Spending spending = getSpending(spendingId, user);
        spending.updateExclude(excludeTotal);
    }

    // 지출 상세 조회
    public GetSpendingDto getSpending(Long spendingId, String loginId) {
        User user = findUser(loginId);
        Spending spending = getSpending(spendingId, user);
        return GetSpendingDto.from(spending);
    }

    /*
        오늘 지출 추천

        1. 카테고리별 오늘까지 총 사용 금액을 조회합니다.

        2. 카테고리의 이번 달 예산을 조회합니다.
            - 예산이 설정되어 있지 않으면 지출 추천이 불가능 하므로 예외처리(예산 설정하라고 메세지를 출력)

        3. 상태 점검
            3-1. 총 사용 금액이 이번 달 예산을 초과 했으면, [최소 추천 금액]을 추천합니다.
            3-2. 0원 썼으면, [이번 달 예산 / 남은 일 수] 금액을 추천합니다.
            3-3. 위 두 상태의 경우 4번 부터의 과정은 필요 없으므로 early return

        4. 하루 적정 사용 금액을 구합니다. (이번 달 예산 / 이번 달의 총 일 수)

        5. 현재까지 적정 금액을 얼마나 초과 했는지 or 절약 했는지 계산합니다. [(지난 일 수 * 하루 적정 사용 금액) - 현재까지 사용 금액]
            5-1. >= 0 : 적정 금액보다 절약
            5-2. < 0  : 적정 금액보다 초과

        6. 적정 금액보다 절약 했을 때 [남은 예산 / 남은 일 수] 금액을 추천합니다.
            - 현재까지의 적정 금액 대비 사용 금액이 70% 이상이면 적당히 절약 중,
              70% 미만이면 완벽히 잘 절약 중으로 분기하여 상태 메세지를 저장합니다.

        7. 적정 금액보다 초과 했을 때 [하루 적정 사용 금액 - (초과금 / 남은 일 수)] 금액을 추천합니다. (초과금을 남은 기간동안 분배하여 부담)

        8. 위에서 구한 추천 금액을 사용자 친화적으로 변환합니다.
            8-1. 최소 추천 금액보다 적으면 [최소 추천 금액]으로 반환합니다.
            8-2. 100원 단위로 반올림합니다.
    */
    public GetSpendingRecommendDto recommendSpending(String loginId) {
        User user = findUser(loginId);

        // 날짜 정보 얻기
        LocalDate today = LocalDate.now();  // 오늘 날짜
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);  // 이번 달의 1일 날짜
        int lengthOfMonth = today.lengthOfMonth();  // 이번 달의 총 일 수
        int pastDayOfMonth = today.getDayOfMonth();  // 이번 달의 지난 일 수 (오늘 기준)
        int remainDayOfMonth = lengthOfMonth - pastDayOfMonth;  // 이번 달의 남은 일 수 (오늘 기준)

        long recommendSpendingTotal = 0;  // 오늘 지출 추천 금액
        List<GetSpendingRecommendByCategoryDto> recommendSpendingByCategory = new ArrayList<>();  // 카테고리별 오늘 지출 추천 금액 및 메세지

        // 1. 카테고리별 오늘까지 총 사용 금액 조회
        List<SpendingRecommendProjection> usedAmountByCategory =
            spendingRepository.getUsedAmountByCategory(getStartDateTime(firstDayOfMonth), getEndDateTime(today), user.getId());
        // 카테고리별로 순회하며 지출 추천
        for (SpendingRecommendProjection projection : usedAmountByCategory) {
            // 2. 카테고리의 이번 달 예산 조회
            Long totalBudgetOfMonth = budgetRepository.findAmountByUserAndCategory(firstDayOfMonth, user, projection.getCategoryName());

            // 설정 되어 있지 않으면 지출 추천 불가능
            if (totalBudgetOfMonth == null) {
                throw new NotSetException(ResponseCode.NOT_SET_BUDGET_FOR_SPENDING_RECOMMEND);
            }

            // 카테고리별 지출 추천
            GetSpendingRecommendByCategoryDto spendingRecommendByCategory = recommendSpendingByCategory(
                projection.getCategoryName(), projection.getUsedAmount(), totalBudgetOfMonth, lengthOfMonth, pastDayOfMonth, remainDayOfMonth);

            recommendSpendingByCategory.add(spendingRecommendByCategory);
            recommendSpendingTotal += spendingRecommendByCategory.getRecommendAmount();
        }

        return GetSpendingRecommendDto.of(recommendSpendingTotal, recommendSpendingByCategory);
    }

    // 카테고리별 지출 추천
    private GetSpendingRecommendByCategoryDto recommendSpendingByCategory(
        String categoryName,
        long usedAmount,
        long totalBudgetOfMonth,
        int lengthOfMonth,
        int pastDayOfMonth,
        int remainDayOfMonth
    ) {
        long recommendAmount;  // 추천 지출 금액
        String message;  // 지출 상태 메세지
        long remainBudget = totalBudgetOfMonth - usedAmount;  // 남은 예산

        // 3-1. 예산 초과 [최소 추천 금액] 추천
        if (remainBudget <= 0) {
            recommendAmount = MIN_RECOMMEND_AMOUNT;
            message = SpendingMessage.OVERSPENT.getMessage();

            return GetSpendingRecommendByCategoryDto.of(categoryName, recommendAmount, message);
        }

        // 3-2. 0원 썼으면 [이번 달 예산 / 남은 일 수] 추천
        if (usedAmount == 0) {
            recommendAmount = totalBudgetOfMonth / remainDayOfMonth;
            message = SpendingMessage.SAVING_WELL.getMessage();

            return GetSpendingRecommendByCategoryDto.of(categoryName, adjustRecommendAmount(recommendAmount), message);
        }

        // 4. 하루 적정 사용 금액 (이번 달 예산 / 이번 달의 총 일 수)
        long budgetOfDay = totalBudgetOfMonth / lengthOfMonth;

        // 5. 현재까지 적정 금액을 얼마나 초과 했는지 or 절약 했는지 계산
        long budgetDifference = pastDayOfMonth * budgetOfDay - usedAmount;  // 현재까지의 초과 or 절약 비용

        // 6. 적정 금액보다 절약 [남은 예산 / 이번 달의 남은 일 수] 추천
        if (budgetDifference >= 0) {
            recommendAmount = remainBudget / remainDayOfMonth;

            // 적정 금액 대비 사용 금액 비율
            long usedRate = usedAmount * 100 / (pastDayOfMonth * budgetOfDay);
            if (usedRate >= SAVING_WELL_RATIO) {  // 70% 이상이면 적당히 절약 중,
                message = SpendingMessage.MODERATE_USAGE.getMessage();
            } else {  // 미만이면 완벽히 잘 절약 중
                message = SpendingMessage.SAVING_WELL.getMessage();
            }
        } else {  // 7. 적정 금액보다 초과 [하루 적정 사용 금액 - (초과금 / 이번 달의 남은 일 수)] 추천 (초과금을 남은 기간동안 분배하여 부담)
            long distributedAmount = Math.abs(budgetDifference / remainDayOfMonth);
            recommendAmount = budgetOfDay - distributedAmount;
            message = SpendingMessage.EXCEEDING_LIMIT.getMessage();
        }

        return GetSpendingRecommendByCategoryDto.of(categoryName, adjustRecommendAmount(recommendAmount), message);  // 8
    }

    // 추천 금액 변환
    private long adjustRecommendAmount(long recommendAmount) {
        // 8. 추천 금액을 사용자 친화적으로 변환
        if (recommendAmount <= MIN_RECOMMEND_AMOUNT) {  // 8-1. 최소 추천 금액보다 적으면 [최소 추천 금액] 추천
            recommendAmount = MIN_RECOMMEND_AMOUNT;
        } else if (recommendAmount % 100 > 0) {  // 8-2. 100원 단위로 반올림
            recommendAmount = (long) Math.floor(recommendAmount / 100.0) * 100;
        }

        return recommendAmount;
    }

    /*
        오늘 지출 안내

        1. 카테고리별 오늘 지출 금액을 조회합니다.
        2. 카테고리별 이번 달 예산을 조회합니다.
        3. 오늘 적정 금액을 구합니다. [이번 달 예산 / 이번 달의 총 일 수]
        4. 위험도를 구합니다. [오늘 지출 금액 / 오늘 적정 금액 * 100]
     */
    public GetSpendingTodayDto spendingToday(String loginId) {
        User user = findUser(loginId);

        long todaySpendingTotal = 0;  // 오늘 지출 총액
        List<GetSpendingTodayByCategoryDto> todaySpendingTotalByCategory = new ArrayList<>();  // 카테고리별 오늘 지출 총 액

        // 날짜 정보 얻기
        LocalDate today = LocalDate.now();  // 오늘 날짜
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);  // 이번 달의 1일 날짜
        int lengthOfMonth = today.lengthOfMonth();  // 이번 달의 총 일 수

        // 1. 카테고리별 오늘 지출 금액을 조회
        List<SpendingTodayProjection> todaySpendingByCategory =
            spendingRepository.getSumOfAmountGroupByCategory(getStartDateTime(today), getEndDateTime(today), user);
        for (SpendingTodayProjection projection : todaySpendingByCategory) {

            // 2. 카테고리의 이번 달 예산 조회
            Long budgetOfMonth = budgetRepository.findAmountByUserAndCategory(firstDayOfMonth, user, projection.getCategory().getName());

            // 3. 오늘 적정 금액을 구합니다.
            long moderateAmount = budgetOfMonth / lengthOfMonth;

            // 오늘 지출 금액
            long todayUsedAmount = projection.getUsedAmount();

            // 4. 위험도를 구합니다.
            long riskRate = Math.round(todayUsedAmount * 100.0 / moderateAmount);

            todaySpendingTotal += todayUsedAmount;
            todaySpendingTotalByCategory.add(
                GetSpendingTodayByCategoryDto.of(projection.getCategory().getName(), moderateAmount, todayUsedAmount, riskRate + "%")
            );
        }

        return GetSpendingTodayDto.of(todaySpendingTotal, todaySpendingTotalByCategory);
    }

    /*
        지출 통계
     */
    public GetSpendingStatisticsDto spendingStatistics(String loginId) {
        User user = findUser(loginId);

        // 날짜 정보 얻기
        LocalDate today = LocalDate.now();  // 오늘 날짜
        LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);  // 이번 달의 1일 날짜
        LocalDate lastMonth = today.minusMonths(1);  // 지난 달 날짜
        LocalDate firstDayOfLastMonth = lastMonth.withDayOfMonth(1);  // 지난 달의 1일 날짜
        LocalDate lastWeek = today.minusWeeks(1);  // 지난 요일 날짜

        // 지난 달 대비 소비율 - 총액 소비율
        long monthlyTotalSpendingRate = calculateMonthlyTotalSpendingRate(today, firstDayOfThisMonth, lastMonth, firstDayOfLastMonth, user);

        // 지난 달 대비 소비율 - 카테고리 별 소비율
        List<GetSpendingMonthlyByCategoryDto> monthlyCategorySpendingRate =
            calculateMonthlyCategorySpendingRate(today, firstDayOfThisMonth, lastMonth, firstDayOfLastMonth, user);

        // 지난 요일 대비 소비율
        long weeklySpendingRate = calculateWeeklySpendingRate(today, lastWeek, user);

        // 다른 유저 대비 소비율
        long spendingRateComparedToOthers = calculateSpendingRateComparedToOthers(today, firstDayOfThisMonth, loginId, user);

        return GetSpendingStatisticsDto.of(
            spendingRateComparedToOthers + "%",
            weeklySpendingRate + "%",
            monthlyTotalSpendingRate + "%",
            monthlyCategorySpendingRate
        );
    }

    /*
     * 지난 달 대비 소비율 - 총액 소비율
     */
    private long calculateMonthlyTotalSpendingRate(
        LocalDate today,
        LocalDate firstDayOfThisMonth,
        LocalDate lastMonth,
        LocalDate firstDayOfLastMonth,
        User user
    ) {
        // 이번 달 사용 금액
        long usedAmountInThisMonth =
            spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(firstDayOfThisMonth), getEndDateTime(today), user);
        // 지난 달 사용 금액
        long usedAmountInLastMonth =
            spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(firstDayOfLastMonth), getEndDateTime(lastMonth), user);

        return calculateRate(usedAmountInLastMonth, usedAmountInThisMonth);
    }

    /*
     * 지난 달 대비 소비율 - 카테고리 별 소비율
     */
    private List<GetSpendingMonthlyByCategoryDto> calculateMonthlyCategorySpendingRate(
        LocalDate today,
        LocalDate firstDayOfThisMonth,
        LocalDate lastMonth,
        LocalDate firstDayOfLastMonth,
        User user
    ) {
        // 카테고리 별 이번 달 사용 금액
        List<SpendingTodayProjection> usedAmountByCategoryInThisMonth =
            spendingRepository.getSumOfAmountGroupByCategory(getStartDateTime(firstDayOfThisMonth), getEndDateTime(today), user);
        // 카테고리 별 지난 달 사용 금액
        List<SpendingTodayProjection> usedAmountByCategoryInLastMonth =
            spendingRepository.getSumOfAmountGroupByCategory(getStartDateTime(firstDayOfLastMonth), getEndDateTime(lastMonth), user);

        List<GetSpendingMonthlyByCategoryDto> monthlyCategorySpendingRate = new ArrayList<>();
        for (int i = 0; i < usedAmountByCategoryInThisMonth.size(); i++) {
            long usedAmountInThisMonth = usedAmountByCategoryInThisMonth.get(i).getUsedAmount();
            long usedAmountInLastMonth = usedAmountByCategoryInLastMonth.get(i).getUsedAmount();
            long spendingRate = calculateRate(usedAmountInLastMonth, usedAmountInThisMonth);

            monthlyCategorySpendingRate.add(
                GetSpendingMonthlyByCategoryDto.of(usedAmountByCategoryInThisMonth.get(i).getCategory().getName(), spendingRate + "%")
            );
        }

        return monthlyCategorySpendingRate;
    }

    /*
     * 지난 요일 대비 소비율 계산
     */
    private long calculateWeeklySpendingRate(LocalDate today, LocalDate lastWeek, User user) {
        // 오늘 사용 금액
        long usedAmountToday = spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(today), getEndDateTime(today), user);
        // 지난 요일 사용 금액
        long usedAmountInLastWeeks = spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(lastWeek), getEndDateTime(lastWeek), user);

        return calculateRate(usedAmountInLastWeeks, usedAmountToday);
    }

    /*
     * 다른 유저 대비 소비율
     */
    private long calculateSpendingRateComparedToOthers(
        LocalDate today,
        LocalDate firstDayOfThisMonth,
        String loginId,
        User user
    ) {
        // 모든 유저들의 예산 대비 사용한 비율의 합
        long rateSum = 0;
        long mySpendingRate = 0;

        // 다른 유저들의 이번 달 총 예산
        List<BudgetTotalProjection> usersBudgetTotal = budgetRepository.getSumOfBudgetGroupByUser(firstDayOfThisMonth);
        for (BudgetTotalProjection budgetTotal : usersBudgetTotal) {
            // 나의 소비율
            if (loginId.equals(budgetTotal.getUser().getLoginId())) {
                // 이번 달 사용 금액
                long usedAmountInThisMonth =
                    spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(firstDayOfThisMonth), getEndDateTime(today), user);
                mySpendingRate = usedAmountInThisMonth * 100 / budgetTotal.getSumOfBudget();
                continue;
            }

            // 오늘까지 소비한 지출
            long usedAmount = spendingRepository.getSumOfAmountByDateAndUser(getStartDateTime(firstDayOfThisMonth),
                getEndDateTime(today), budgetTotal.getUser());

            // 다른 유저의 소비율
            long spendingRate = usedAmount * 100 / budgetTotal.getSumOfBudget();
            rateSum += spendingRate;
        }
        int userCnt = usersBudgetTotal.size();
        long rateAverage = rateSum / (userCnt - 1);

        return mySpendingRate * 100 / rateAverage;
    }

    /*
        지출 목록 조회 (with Java)
        java 로 처리 로직 구현
     */
    @Transactional(readOnly = true)
    public GetSpendingListDto getSpendingList(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = findUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        List<GetSpendingDto> spendingList =
            spendingRepository.findByDateBetweenOrderByDateDesc(startDateTime, endDateTime, category, minAmount, maxAmount, user).stream()
                .map(GetSpendingDto::from)
                .toList();

        // 카테고리 목록을 조회하여 카테고리명=금액 map 을 만든다.
        // -> 0원 쓴 카테고리도 조회 결과로 보여주기 위해서.
        List<GetCategoryDto> categoryList = categoryRepository.findAll().stream()
            .map(GetCategoryDto::from)
            .toList();

        Map<String, Long> map = categoryList.stream()
            .collect(Collectors.toMap(GetCategoryDto::getName, categoryDto -> 0L));

        long spendingTotal = 0;
        for (GetSpendingDto spending : spendingList) {
            if (spending.getExcludeTotal() == YnColumn.Y) {
                continue;
            }

            spendingTotal += spending.getAmount();  // 지출 합계
            map.replace(spending.getCategoryName(), map.get(spending.getCategoryName()) + spending.getAmount());  // 카테고리별 지출합계
        }

        // map to List<GetSpendingSummaryDto>
        List<GetSpendingSummaryDto> spendingTotalByCategory = new ArrayList<>();
        for (Entry<String, Long> entry : map.entrySet()) {
            spendingTotalByCategory.add(GetSpendingSummaryDto.of(entry.getKey(), entry.getValue()));
        }

        return GetSpendingListDto.of(spendingTotal, spendingTotalByCategory, ListResponseDto.from(spendingList));
    }

    /*
        지출 목록 조회 (with Query)
        쿼리로 모든 응답 값들을 조회
     */
    @Transactional(readOnly = true)
    public List<GetSpendingDto> getSpendingListWithQuery(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = findUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.findByDateBetweenOrderByDateDesc(startDateTime, endDateTime, category, minAmount, maxAmount, user).stream()
            .map(GetSpendingDto::from)
            .toList();
    }

    // 카테고리 별 지출 합계
    @Transactional(readOnly = true)
    public List<GetSpendingSummaryDto> getSumOfAmountByCategory(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = findUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        int categorySize = (category != null) ? category.size() : 0;

        return spendingRepository
            .getSumOfAmountGroupByCategory(startDateTime, endDateTime, categorySize, category, minAmount, maxAmount, user.getId()).stream()
            .map(projection -> GetSpendingSummaryDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }

    // 모든 지출 합계
    @Transactional(readOnly = true)
    public long getSumOfAmountByDate(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = findUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.getSumOfAmountByDate(startDateTime, endDateTime, category, minAmount, maxAmount, user);
    }

    // 지출 삭제
    @Transactional
    public void deleteSpending(Long spendingId, String loginId) {
        User user = findUser(loginId);
        Spending spending = getSpending(spendingId, user);
        spendingRepository.delete(spending);
    }

    // 지출 id 로 조회
    private Spending getSpending(Long spendingId, User user) {
        return spendingRepository.findByIdAndUser(spendingId, user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_SPENDING));
    }

    // 해당 날짜의 시작 시점 (00:00)
    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return startDate.atStartOfDay();
    }

    // 해당 날짜의 끝 시점 (23:59:59)
    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate.atTime(LocalTime.MAX).withNano(0);
    }

    private User findUser(String loginId) {
        return userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    private Category findCategory(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY));
    }

    // 비율 구하기
    private long calculateRate(long previousAmount, long currentAmount) {
        if (previousAmount == 0) {
            return currentAmount;
        }
        return currentAmount * 100 / previousAmount;
    }
}
