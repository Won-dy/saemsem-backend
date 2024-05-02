package com.wealdy.saemsembackend.domain.budget.service;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.service.dto.BudgetSummaryDto;
import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Transactional
    public void createBudget(LocalDate date, List<BudgetSummaryDto> getBudgetDtoList, String loginId) {
        getBudgetDtoList
            .forEach(getBudgetDto -> {
                User user = userService.getUser(loginId);
                Category category = categoryService.getCategory(getBudgetDto.getCategoryName());
                Optional<Budget> findBudget = budgetRepository.findByDateAndCategoryAndUser(date, category, user);
                findBudget.ifPresentOrElse(
                    budget -> budget.updateBudget(getBudgetDto.getAmount()),
                    () -> createBudget(date, getBudgetDto, user, category)
                );
            });
    }

    private void createBudget(LocalDate date, BudgetSummaryDto getBudgetDto, User user, Category category) {
        Budget budget = Budget.createBudget(date, getBudgetDto.getAmount(), user, category);
        budgetRepository.save(budget);
    }

    @Transactional(readOnly = true)
    public List<GetBudgetDto> getBudgetList(LocalDate date, String loginId) {
        User user = userService.getUser(loginId);
        return budgetRepository.findByDateAndUser(date, user).stream()
            .map(projection -> GetBudgetDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }
}
