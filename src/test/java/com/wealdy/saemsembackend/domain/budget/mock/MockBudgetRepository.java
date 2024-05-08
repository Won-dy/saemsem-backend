package com.wealdy.saemsembackend.domain.budget.mock;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetRecommendProjection;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetSummaryProjection;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetTotalProjection;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockBudgetRepository implements BudgetRepository {

    private final List<Budget> budgetList = new ArrayList<>();
    private final List<BudgetSummaryProjection> budgetSummaryProjection = new ArrayList<>();

    @Override
    public Budget save(Budget budget) {
        budgetList.removeIf(removeBudget -> {
            return removeBudget.getId().equals(budget.getId());
        });
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
    public List<BudgetSummaryProjection> findByDateAndUser(LocalDate date, User user) {
        budgetSummaryProjection.clear();
        budgetSummaryProjection.add(
            new BudgetSummaryProjection() {
                @Override
                public String getCategoryName() {
                    return "기타";
                }

                @Override
                public Long getAmount() {
                    return 100000L;
                }
            }
        );
        budgetSummaryProjection.add(
            new BudgetSummaryProjection() {
                @Override
                public String getCategoryName() {
                    return "식비";
                }

                @Override
                public Long getAmount() {
                    return 333333L;
                }
            }
        );
        return budgetSummaryProjection;
    }

    @Override
    public List<BudgetRecommendProjection> findByDate(LocalDate date) {
        return null;
    }

    @Override
    public List<BudgetTotalProjection> sumByUser(LocalDate date) {
        return null;
    }
}
