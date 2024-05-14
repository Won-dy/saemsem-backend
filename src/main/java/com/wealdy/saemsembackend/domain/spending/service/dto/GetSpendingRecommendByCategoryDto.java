package com.wealdy.saemsembackend.domain.spending.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingRecommendByCategoryDto {

    private final String categoryName;
    private final long recommendAmount;
    private String message;

    public static GetSpendingRecommendByCategoryDto of(String categoryName, long recommendAmount, String message) {
        return new GetSpendingRecommendByCategoryDto(categoryName, recommendAmount, message);
    }
}
