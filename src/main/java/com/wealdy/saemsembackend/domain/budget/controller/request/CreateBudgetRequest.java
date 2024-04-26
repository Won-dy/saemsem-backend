package com.wealdy.saemsembackend.domain.budget.controller.request;

import com.wealdy.saemsembackend.domain.budget.service.dto.BudgetSummaryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class CreateBudgetRequest {

    @Positive(message = "년도는 양수로 입력해야 합니다.")
    private int year;

    @Range(min = 1, max = 12, message = "월은 1~12 중 하나로 입력해야 합니다.")
    private int month;

    @NotEmpty(message = "예산은 필수 값입니다.")
    private List<@Valid BudgetSummaryDto> budgets;
}
