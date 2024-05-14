package com.wealdy.saemsembackend.domain.spending.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingTodayByCategoryDto {

    private final String categoryName;
    private final long moderateAmount;
    private final long todayUsedAmount;
    private String riskRate;

    public static GetSpendingTodayByCategoryDto of(String categoryName, long moderateAmount, long todayUsedAmount, String riskRate) {
        return new GetSpendingTodayByCategoryDto(categoryName, moderateAmount, todayUsedAmount, riskRate);
    }
}
