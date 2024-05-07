package com.wealdy.saemsembackend.domain.spending.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_SPENDING;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.category.service.dto.GetCategoryDto;
import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import com.wealdy.saemsembackend.domain.spending.repository.SpendingRepository;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingListDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingSummaryDto;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpendingService {

    private final SpendingRepository spendingRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional
    public Long createSpending(LocalDateTime date, long amount, String memo, boolean excludeTotal, String categoryName, String loginId) {
        User user = userService.getUser(loginId);
        Category category = categoryService.getCategory(categoryName);

        Spending spending = spendingRepository.save(Spending.createSpending(date, amount, memo, excludeTotal, user, category));
        return spending.getId();
    }

    @Transactional
    public void updateSpending(
        Long spendingId, LocalDateTime date, long amount, String memo, String categoryName, String loginId
    ) {
        User user = userService.getUser(loginId);
        Category category = categoryService.getCategory(categoryName);

        Spending spending = getSpending(spendingId, user);
        spending.updateSpending(date, amount, memo, category);
    }

    @Transactional
    public void updateExclude(Long spendingId, boolean excludeTotal, String loginId) {
        User user = userService.getUser(loginId);

        Spending spending = getSpending(spendingId, user);
        spending.updateExclude(excludeTotal);
    }

    public GetSpendingDto getSpending(Long spendingId, String loginId) {
        User user = userService.getUser(loginId);
        Spending spending = getSpending(spendingId, user);
        return GetSpendingDto.from(spending);
    }

    @Transactional(readOnly = true)
    public GetSpendingListDto getSpendingList(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = userService.getUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        List<GetSpendingDto> spendingList =
            spendingRepository.findByDateBetweenOrderByDateDesc(startDateTime, endDateTime, category, minAmount, maxAmount, user).stream()
                .map(GetSpendingDto::from)
                .toList();

        // 카테고리 목록을 조회하여 카테고리명=금액 map 을 만든다.
        // -> 0원 쓴 카테고리도 조회 결과로 보여주기 위해서.
        Map<String, Long> map = new HashMap<>();
        List<GetCategoryDto> categoryList = categoryService.getCategoryList();
        for (GetCategoryDto categoryDto : categoryList) {
            map.put(categoryDto.getName(), 0L);
        }

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

    // 쿼리로 모든 응답 값들을 조회
    @Transactional(readOnly = true)
    public List<GetSpendingDto> getSpendingListWithQuery(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = userService.getUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.findByDateBetweenOrderByDateDesc(startDateTime, endDateTime, category, minAmount, maxAmount, user).stream()
            .map(GetSpendingDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<GetSpendingSummaryDto> getSumOfAmountByCategory(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = userService.getUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        int categorySize = (category != null) ? category.size() : 0;

        return spendingRepository.getSumOfAmountByCategory(startDateTime, endDateTime, categorySize, category, minAmount, maxAmount, user.getId())
            .stream()
            .map(projection -> GetSpendingSummaryDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }

    @Transactional(readOnly = true)
    public long getSumOfAmountByDate(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String loginId
    ) {
        User user = userService.getUser(loginId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.getSumOfAmountByDate(startDateTime, endDateTime, category, minAmount, maxAmount, user);
    }

    @Transactional
    public void deleteSpending(Long spendingId, String loginId) {
        User user = userService.getUser(loginId);
        Spending spending = getSpending(spendingId, user);
        spendingRepository.delete(spending);
    }

    private Spending getSpending(Long spendingId, User user) {
        return spendingRepository.findByIdAndUser(spendingId, user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_SPENDING));
    }

    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return LocalDate.parse(startDate.toString()).atStartOfDay();
    }

    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate.atTime(LocalTime.MAX).withNano(0);
    }
}
