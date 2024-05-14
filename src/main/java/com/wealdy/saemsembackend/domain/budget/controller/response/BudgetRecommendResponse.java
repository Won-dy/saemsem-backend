package com.wealdy.saemsembackend.domain.budget.controller.response;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BudgetRecommendResponse {

    private final LocalDate date;
    private final long budgetTotal;
    private final List<GetBudgetDto> budgets;

    public static BudgetRecommendResponse of(LocalDate date, long budgetTotal, List<GetBudgetDto> budgets) {
        return new BudgetRecommendResponse(date, budgetTotal, budgets);
    }
}
