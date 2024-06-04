package com.wealdy.saemsembackend.domain.budget.repository;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
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

    @Query(value = "select b from Budget b join fetch b.category where b.date = :date and b.user = :user")
    List<Budget> findByDateAndUser(LocalDate date, User user);

    @Query(value = "select b from Budget b join fetch b.category where b.date = :date")
    List<Budget> findByDate(LocalDate date);

    @Query(value = "select b.user as user, sum(b.amount) as sumOfBudget from Budget b "
        + "where b.date = :date group by b.user")
    List<BudgetTotalProjection> getSumOfBudgetByDateGroupByUser(@Param("date") LocalDate date);

    @Query(value = "select b.amount from Budget b where b.date = :date and b.user = :user and b.category.name = :category")
    Long findAmountByUserAndCategory(@Param("date") LocalDate date, @Param("user") User user, @Param("category") String category);
}
