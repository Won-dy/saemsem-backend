package com.wealdy.saemsembackend.domain.budget.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wealdy.saemsembackend.domain.budget.entity.Budget;
import com.wealdy.saemsembackend.domain.budget.mock.MockBudgetRepository;
import com.wealdy.saemsembackend.domain.budget.repository.BudgetRepository;
import com.wealdy.saemsembackend.domain.budget.service.dto.BudgetSummaryDto;
import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.category.mock.MockCategoryRepository;
import com.wealdy.saemsembackend.domain.category.repository.CategoryRepository;
import com.wealdy.saemsembackend.domain.category.service.CategoryService;
import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.core.util.DateUtils;
import com.wealdy.saemsembackend.domain.user.entity.User;
import com.wealdy.saemsembackend.domain.user.mock.MockUserRepository;
import com.wealdy.saemsembackend.domain.user.repository.UserRepository;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetServiceTest {

    BudgetRepository budgetRepository;
    BudgetService budgetService;
    CategoryService categoryService;
    CategoryRepository categoryRepository;
    UserService userService;
    UserRepository userRepository;


    @BeforeEach
    void init() {
        categoryRepository = new MockCategoryRepository();
        categoryService = new CategoryService(categoryRepository);

        userRepository = new MockUserRepository();
        userService = new UserService(userRepository);

        budgetRepository = new MockBudgetRepository();
        budgetService = new BudgetService(budgetRepository, categoryService, userService);
    }

    @DisplayName("[getBudgetList] 예산 목록 조회에 성공한다.")
    @Test
    void getBudgetList() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(user);

        Category category1 = Category.of(1L, "기타");
        categoryRepository.save(category1);

        Category category2 = Category.of(2L, "식비");
        categoryRepository.save(category2);

        Budget budget = Budget.of(1L, DateUtils.convertFirstDayOfMonth(2024, 1), 100000, user, category1);
        budgetRepository.save(budget);

        Budget budget2 = Budget.of(2L, DateUtils.convertFirstDayOfMonth(2024, 1), 333333, user, category2);
        budgetRepository.save(budget2);

        // when
        List<GetBudgetDto> budgetList = budgetService.getBudgetList(DateUtils.convertFirstDayOfMonth(2024, 1), "loginId");

        // then
        assertThat(budgetList.size()).isEqualTo(2);
        assertThat(budgetList.get(0).getCategoryName()).isEqualTo("기타");
        assertThat(budgetList.get(1).getCategoryName()).isEqualTo("식비");
        assertThat(budgetList.get(0).getAmount()).isEqualTo(100000);
        assertThat(budgetList.get(1).getAmount()).isEqualTo(333333);
    }

    @DisplayName("[createBudget] 예산 추가에 성공한다.")
    @Test
    void createBudget() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(user);

        Category category = Category.of(1L, "기타");
        categoryRepository.save(category);

        List<BudgetSummaryDto> budgetSummaryDtoList = new ArrayList<>();
        budgetSummaryDtoList.add(
            new BudgetSummaryDto() {
                @Override
                public String getCategoryName() {
                    return "기타";
                }

                @Override
                public long getAmount() {
                    return 200000;
                }
            }
        );

        // when
        budgetService.createBudget(DateUtils.convertFirstDayOfMonth(2024, 1), budgetSummaryDtoList, user.getLoginId());
        Optional<Budget> budget = budgetRepository.findByDateAndCategoryAndUser(
            DateUtils.convertFirstDayOfMonth(2024, 1), category, user);

        // then
        assertThat(budget.isPresent()).isTrue();
        assertThat(budget.get().getDate()).isEqualTo(DateUtils.convertFirstDayOfMonth(2024, 1));
        assertThat(budget.get().getAmount()).isEqualTo(200000);
        assertThat(budget.get().getUser()).isEqualTo(user);
        assertThat(budget.get().getCategory()).isEqualTo(category);
    }

    @DisplayName("[updateBudget] 이미 존재하는 예산은 금액만 수정 된다.")
    @Test
    void updateBudget() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        userRepository.save(user);

        Category category = Category.of(1L, "기타");
        categoryRepository.save(category);

        List<BudgetSummaryDto> budgetSummaryDtoList = new ArrayList<>();
        budgetSummaryDtoList.add(
            new BudgetSummaryDto() {
                @Override
                public String getCategoryName() {
                    return "기타";
                }

                @Override
                public long getAmount() {
                    return 200000;
                }
            }
        );
        budgetService.createBudget(DateUtils.convertFirstDayOfMonth(2024, 1), budgetSummaryDtoList, user.getLoginId());

        // when
        List<BudgetSummaryDto> budgetSummaryDtoList2 = new ArrayList<>();
        budgetSummaryDtoList2.add(
            new BudgetSummaryDto() {
                @Override
                public String getCategoryName() {
                    return "기타";
                }

                @Override
                public long getAmount() {
                    return 400000;
                }
            }
        );
        budgetService.createBudget(DateUtils.convertFirstDayOfMonth(2024, 1), budgetSummaryDtoList2, user.getLoginId());

        Optional<Budget> budget = budgetRepository.findByDateAndCategoryAndUser(
            DateUtils.convertFirstDayOfMonth(2024, 1), category, user);

        // then
        assertThat(budget.isPresent()).isTrue();
        assertThat(budget.get().getDate()).isEqualTo(DateUtils.convertFirstDayOfMonth(2024, 1));
        assertThat(budget.get().getAmount()).isEqualTo(400000);
        assertThat(budget.get().getUser()).isEqualTo(user);
        assertThat(budget.get().getCategory()).isEqualTo(category);
    }
}