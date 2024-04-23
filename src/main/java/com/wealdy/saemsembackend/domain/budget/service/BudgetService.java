package com.wealdy.saemsembackend.domain.budget.service;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
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
    public void createBudget(LocalDate date, List<GetBudgetDto> getBudgetDtoList, String userId) {
        getBudgetDtoList
            .forEach(getBudgetDto -> {
                User user = userService.getUserById(userId);
                Category category = categoryService.getCategory(getBudgetDto.getCategoryName());
                Optional<Budget> findBudget = budgetRepository.findByDateAndCategoryAndUser(date, category, user);
                if (findBudget.isEmpty()) {
                    Budget budget = Budget.createBudget(
                        date,
                        getBudgetDto.getAmount(),
                        user,
                        category
                    );
                    budgetRepository.save(budget);
                } else {
                    findBudget.get().updateBudget(getBudgetDto.getAmount());
                }
            });
    }

    public List<GetBudgetDto> getBudgetList(LocalDate date, String userId) {
        User user = userService.getUserById(userId);
        return budgetRepository.findByDateAndUser(date, user).stream()
            .map(projection -> GetBudgetDto.of(projection.getCategoryName(), projection.getAmount()))
            .toList();
    }
}
