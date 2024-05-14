package com.wealdy.saemsembackend.domain.budget.repository.projection;

public interface BudgetRecommendProjection {

    Long getUserId();

    String getCategoryName();

    Long getAmount();
}
