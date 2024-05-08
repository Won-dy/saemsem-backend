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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultBudgetRepository implements BudgetRepository {

    private final JpaBudgetRepository jpaBudgetRepository;

    @Override
    public Budget save(Budget budget) {
        return jpaBudgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> findByDateAndCategoryAndUser(LocalDate date, Category category, User user) {
        return jpaBudgetRepository.findByDateAndCategoryAndUser(date, category, user);
    }

    @Override
    public List<BudgetSummaryProjection> findByDateAndUser(LocalDate date, User user) {
        return jpaBudgetRepository.findByDateAndUser(date, user);
    }

    @Override
    public List<BudgetRecommendProjection> findByDate(LocalDate date) {
        return jpaBudgetRepository.findByDate(date);
    }

    @Override
    public List<BudgetTotalProjection> sumByUser(LocalDate date) {
        return jpaBudgetRepository.sumByUser(date);
    }
}
