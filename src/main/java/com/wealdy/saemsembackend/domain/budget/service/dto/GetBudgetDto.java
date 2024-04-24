package com.wealdy.saemsembackend.domain.budget.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetBudgetDto {

    @NotBlank(message = "카테고리는 필수 값입니다.")
    private final String categoryName;

    @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.")
    private final long amount;

    public static GetBudgetDto of(String categoryName, long amount) {
        return new GetBudgetDto(categoryName, amount);
    }
}
