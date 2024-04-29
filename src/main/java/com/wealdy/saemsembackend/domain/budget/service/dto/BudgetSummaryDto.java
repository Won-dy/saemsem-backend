package com.wealdy.saemsembackend.domain.budget.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class BudgetSummaryDto {

    @NotBlank(message = "카테고리는 필수 값입니다.")
    private String categoryName;

    @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.")
    private long amount;
}
