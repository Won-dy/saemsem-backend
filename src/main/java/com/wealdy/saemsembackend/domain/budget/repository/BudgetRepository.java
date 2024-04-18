package com.wealdy.saemsembackend.domain.budget.repository;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByDateAndCategoryAndUser(LocalDate date, Category category, User user);
}
