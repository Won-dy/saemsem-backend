package com.wealdy.saemsembackend.domain.budget.repository;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Override
    Budget save(Budget budget);
}
