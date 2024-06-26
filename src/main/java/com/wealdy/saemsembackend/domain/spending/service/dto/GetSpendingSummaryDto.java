package com.wealdy.saemsembackend.domain.spending.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingSummaryDto {

    private final String categoryName;
    private final long amount;

    public static GetSpendingSummaryDto of(String categoryName, long amount) {
        return new GetSpendingSummaryDto(categoryName, amount);
    }
}
