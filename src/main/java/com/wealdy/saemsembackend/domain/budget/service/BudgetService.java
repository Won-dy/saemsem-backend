package com.wealdy.saemsembackend.domain.budget.service;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Transactional
    public void createBudget(LocalDate date, List<GetBudgetDto> getBudgetDtoList, String userId) {
        getBudgetDtoList
            .forEach(getBudgetDto -> {
                User user = userRepository.findById(userId).get(0);
                Category category = categoryService.findByName(getBudgetDto.getCategoryName());
                Budget findBudget = findByDateAndCategoryAndUser(date, category, user);
                if (findBudget == null) {
                    Budget budget = Budget.createBudget(
                        date,
                        getBudgetDto.getAmount(),
                        user,
                        category
                    );
                    budgetRepository.save(budget);
                } else {
                    findBudget.updateBudget(getBudgetDto.getAmount());
                }
            });
    }

    private Budget findByDateAndCategoryAndUser(LocalDate date, Category category, User user) {
        return budgetRepository.findByDateAndCategoryAndUser(date, category, user);
    }
}
