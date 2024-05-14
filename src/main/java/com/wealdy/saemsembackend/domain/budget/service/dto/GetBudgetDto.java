package com.wealdy.saemsembackend.domain.budget.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetBudgetDto {

    private final String categoryName;
    private final long amount;

    public static GetBudgetDto of(String categoryName, long amount) {
        return new GetBudgetDto(categoryName, amount);
    }
}
