package com.wealdy.saemsembackend.domain.spending.service;

import static com.wealdy.saemsembackend.domain.core.response.ResponseCode.NOT_FOUND_SPENDING;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.core.exception.NotFoundException;
import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import com.wealdy.saemsembackend.domain.spending.repository.SpendingRepository;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingSummaryDto;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
    public Long createSpending(LocalDateTime date, long amount, String memo, boolean excludeTotal, String categoryName, String userId) {
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategory(categoryName);

        Spending spending = spendingRepository.save(Spending.createSpending(date, amount, memo, excludeTotal, user, category));
        return spending.getId();
    }

    @Transactional
    public void updateSpending(
        Long spendingId, LocalDateTime date, long amount, String memo, String categoryName, String userId
    ) {
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategory(categoryName);

        Spending spending = getSpending(spendingId, user);
        spending.updateSpending(date, amount, memo, category);
    }

    @Transactional
    public void updateExclude(Long spendingId, boolean excludeTotal, String userId) {
        User user = userService.getUserById(userId);

        Spending spending = getSpending(spendingId, user);
        spending.updateExclude(excludeTotal);
    }

    public GetSpendingDto getSpending(Long spendingId, String userId) {
        User user = userService.getUserById(userId);
        Spending spending = getSpending(spendingId, user);
        return GetSpendingDto.from(spending);
    }

    @Transactional(readOnly = true)
    public List<GetSpendingDto> getSpendingList(
        LocalDate startDate,
        LocalDate endDate,
        List<String> category,
        Long minAmount,
        Long maxAmount,
        String userId
    ) {
        User user = userService.getUserById(userId);

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
        String userId
    ) {
        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        int categorySize = (category != null) ? category.size() : 0;

        return spendingRepository.getSumOfAmountByCategory(startDateTime, endDateTime, categorySize, category, minAmount, maxAmount, userId).stream()
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
        String userId
    ) {
        User user = userService.getUserById(userId);

        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.getSumOfAmountByDate(startDateTime, endDateTime, category, minAmount, maxAmount, user);
    }

    @Transactional
    public void deleteSpending(Long spendingId, String userId) {
        User user = userService.getUserById(userId);
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
