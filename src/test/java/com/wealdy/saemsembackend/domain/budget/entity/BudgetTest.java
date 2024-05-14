package com.wealdy.saemsembackend.domain.budget.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.core.util.DateUtils;
import com.wealdy.saemsembackend.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @DisplayName("[createBudget] 지출 생성에 성공한다.")
    @Test
    void createBudget() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        Category category = Category.of(1L, "기타");

        // when
        Budget budget = Budget.createBudget(
            DateUtils.convertFirstDayOfMonth(2024, 1),
            100000,
            user,
            category
        );

        // then
        assertThat(budget.getDate()).isEqualTo(DateUtils.convertFirstDayOfMonth(2024, 1));
        assertThat(budget.getAmount()).isEqualTo(100000);
        assertThat(budget.getUser()).isEqualTo(user);
        assertThat(budget.getCategory()).isEqualTo(category);
    }

    @DisplayName("[of] id 포함 지출 생성에 성공한다.")
    @Test
    void of() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        Category category = Category.of(1L, "기타");

        // when
        Budget budget = Budget.of(
            1L,
            DateUtils.convertFirstDayOfMonth(2024, 1),
            100000,
            user,
            category
        );

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getDate()).isEqualTo(DateUtils.convertFirstDayOfMonth(2024, 1));
        assertThat(budget.getAmount()).isEqualTo(100000);
        assertThat(budget.getUser()).isEqualTo(user);
        assertThat(budget.getCategory()).isEqualTo(category);
    }

    @DisplayName("[updateBudget] 지출 금액 수정에 성공한다.")
    @Test
    void updateBudget() {
        // given
        User user = new User(1L, "loginId", "1234", "nickname", YnColumn.N, null);
        Category category = Category.of(1L, "기타");

        Budget budget = Budget.createBudget(
            DateUtils.convertFirstDayOfMonth(2024, 1),
            100000,
            user,
            category
        );

        // when
        budget.updateBudget(200000);

        // then
        assertThat(budget.getDate()).isEqualTo(DateUtils.convertFirstDayOfMonth(2024, 1));
        assertThat(budget.getAmount()).isEqualTo(200000);
        assertThat(budget.getUser()).isEqualTo(user);
        assertThat(budget.getCategory()).isEqualTo(category);
    }
}