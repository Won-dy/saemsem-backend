package com.wealdy.saemsembackend.domain.spending.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingStatisticsDto {

    private final String spendingRateComparedToOthers;
    private final String weeklySpendingRate;
    private final String monthlyTotalSpendingRate;
    private final List<GetSpendingMonthlyByCategoryDto> monthlyCategorySpendingRate;

    public static GetSpendingStatisticsDto of(
        String spendingRateComparedToOthers,
        String weeklySpendingRate,
        String monthlyTotalSpendingRate,
        List<GetSpendingMonthlyByCategoryDto> monthlyCategorySpendingRate
    ) {
        return new GetSpendingStatisticsDto(spendingRateComparedToOthers, weeklySpendingRate, monthlyTotalSpendingRate, monthlyCategorySpendingRate);
    }
}
