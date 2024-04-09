package com.wealdy.saemsembackend.domain.budget.service;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
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
    public void createBudget(LocalDateTime date, List<GetBudgetDto> getBudgetDtoList) {
        getBudgetDtoList
            .forEach(getBudgetDto -> {
                // todo: 로그인 한 사용자 정보 조회
                User user = userRepository.findByLoginId("test1").get(0);
                Category category = categoryService.findByName(getBudgetDto.getCategory());
                Budget budget = Budget.createBudget(
                    date,
                    getBudgetDto.getAmount(),
                    user,
                    category
                );
                budgetRepository.save(budget);
            });
    }
}
