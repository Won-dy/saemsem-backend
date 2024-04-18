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
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public Long createSpending(LocalDateTime date, long amount, String memo, boolean excludeTotal, String categoryName, String userId) {
        User user = userRepository.findById(userId).get(0);
        Category category = categoryService.getCategoryByName(categoryName);

        Spending spending = spendingRepository.save(Spending.createSpending(date, amount, memo, excludeTotal, user, category));
        return spending.getId();
    }

    public GetSpendingDto getSpending(Long spendingId) {
        Spending spending = spendingRepository.findById(spendingId).orElseThrow(() -> new NotFoundException(NOT_FOUND_SPENDING));
        return GetSpendingDto.from(spending);
    }

    public List<GetSpendingDto> getSpendingList(LocalDate startDate, LocalDate endDate, List<String> category, Long minAmount, Long maxAmount) {
        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.findByDateBetweenOrderByDateDesc(startDateTime, endDateTime).stream()
            .map(GetSpendingDto::from)
            .toList();
    }

    public List<GetSpendingSummaryDto> getSumOfAmountByCategory(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.getSumOfAmountByCategory(startDateTime, endDateTime).stream()
            .map(projection -> GetSpendingSummaryDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }

    public long getSumOfAmountByDate(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = getStartDateTime(startDate);
        LocalDateTime endDateTime = getEndDateTime(endDate);

        return spendingRepository.getSumOfAmountByDate(startDateTime, endDateTime);
    }

    @Transactional
    public void deleteSpending(Long spendingId) {
        Spending spending = spendingRepository.findById(spendingId).orElseThrow(() -> new NotFoundException(NOT_FOUND_SPENDING));
        spendingRepository.delete(spending);
    }

    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return LocalDate.parse(startDate.toString()).atStartOfDay();
    }

    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate.atTime(LocalTime.MAX).withNano(0);
    }
}
