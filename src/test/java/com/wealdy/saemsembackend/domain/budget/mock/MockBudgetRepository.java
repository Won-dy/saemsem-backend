package com.wealdy.saemsembackend.domain.budget.mock;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetTotalProjection;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockBudgetRepository implements BudgetRepository {

    private final List<Budget> budgetList = new ArrayList<>();

    @Override
    public Budget save(Budget budget) {
        budgetList.removeIf(removeBudget -> removeBudget.getId().equals(budget.getId()));
        budgetList.add(budget);
        return budget;
    }

    @Override
    public Optional<Budget> findByDateAndCategoryAndUser(LocalDate date, Category category, User user) {
        return budgetList.stream()
            .filter(budget -> budget.getDate().equals(date))
            .filter(budget -> budget.getCategory().equals(category))
            .filter(budget -> budget.getUser().equals(user))
            .findFirst();
    }

    @Override
    public List<Budget> findByDateAndUser(LocalDate date, User user) {
        return budgetList.stream()
            .filter(budget -> budget.getDate().equals(date))
            .filter(budget -> budget.getUser().equals(user))
            .toList();
    }

    @Override
    public List<Budget> findByDate(LocalDate date) {
        return null;
    }

    @Override
    public List<BudgetTotalProjection> getSumOfBudgetGroupByUser(LocalDate date) {
        return null;
    }

    @Override
    public Long findAmountByUserAndCategory(LocalDate date, User user, String categoryId) {
        return null;
    }
}
