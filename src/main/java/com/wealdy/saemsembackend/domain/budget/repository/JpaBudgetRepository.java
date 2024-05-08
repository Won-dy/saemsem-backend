package com.wealdy.saemsembackend.domain.budget.repository;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetRecommendProjection;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetSummaryProjection;
import com.wealdy.saemsembackend.domain.budget.repository.projection.BudgetTotalProjection;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaBudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByDateAndCategoryAndUser(LocalDate date, Category category, User user);

    @Query(value = "select c.name as categoryName, coalesce(b.amount, 0) as amount from Category c "
        + "left join Budget b on c.id = b.category.id and b.date = :date and b.user = :user")
    List<BudgetSummaryProjection> findByDateAndUser(@Param("date") LocalDate date, @Param("user") User user);

    @Query(value = "select b.user.id as userId, c.name as categoryName, b.amount as amount from Category c "
        + "left join Budget b on c.id = b.category.id and b.date = :date")
    List<BudgetRecommendProjection> findByDate(@Param("date") LocalDate date);

    @Query(value = "select b.user.id as userId, sum(b.amount) as sumOfBudget from Budget b "
        + "where b.date = :date group by b.user")
    List<BudgetTotalProjection> sumByUser(@Param("date") LocalDate date);
}
