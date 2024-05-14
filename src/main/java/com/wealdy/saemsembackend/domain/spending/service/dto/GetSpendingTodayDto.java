package com.wealdy.saemsembackend.domain.spending.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingTodayDto {

    private final long todaySpendingTotal;
    private final List<GetSpendingTodayByCategoryDto> todaySpendingTotalByCategory;

    public static GetSpendingTodayDto of(long todaySpendingTotal, List<GetSpendingTodayByCategoryDto> todaySpendingTotalByCategory) {
        return new GetSpendingTodayDto(todaySpendingTotal, todaySpendingTotalByCategory);
    }
}
