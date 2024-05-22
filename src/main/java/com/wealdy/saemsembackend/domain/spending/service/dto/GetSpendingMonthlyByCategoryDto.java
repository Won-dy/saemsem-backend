package com.wealdy.saemsembackend.domain.spending.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingMonthlyByCategoryDto {

    private final String categoryName;
    private String spendingRate;

    public static GetSpendingMonthlyByCategoryDto of(String categoryName, String spendingRate) {
        return new GetSpendingMonthlyByCategoryDto(categoryName, spendingRate);
    }
}
