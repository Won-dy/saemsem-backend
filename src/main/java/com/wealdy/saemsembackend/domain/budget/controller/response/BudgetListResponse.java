package com.wealdy.saemsembackend.domain.budget.controller.response;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BudgetListResponse {

    private final LocalDate date;
    private final List<GetBudgetDto> budgets;

    public static BudgetListResponse of(LocalDate date, List<GetBudgetDto> budgets) {
        return new BudgetListResponse(date, budgets);
    }
}
