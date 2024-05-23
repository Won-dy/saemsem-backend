package com.wealdy.saemsembackend.domain.budget.repository;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetTotalProjection;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository {

    Budget save(Budget budget);

    Optional<Budget> findByDateAndCategoryAndUser(LocalDate date, Category category, User user);

    List<Budget> findByDateAndUser(LocalDate date, User user);

    List<Budget> findByDate(LocalDate date);

    List<BudgetTotalProjection> getSumOfBudgetGroupByUser(LocalDate date);

    Long findAmountByUserAndCategory(LocalDate date, User user, String categoryId);
}
