package com.wealdy.saemsembackend.domain.budget.repository.projection;

import com.wealdy.saemsembackend.domain.user.entity.User;

public interface BudgetTotalProjection {

    User getUser();

    Long getSumOfBudget();
}
