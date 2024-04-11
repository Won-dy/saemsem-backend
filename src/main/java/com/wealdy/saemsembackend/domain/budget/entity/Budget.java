package com.wealdy.saemsembackend.domain.budget.entity;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id", nullable = false)
    private Long id;  // 예산 id

    @Column(nullable = false)
    private LocalDate date;  // 예산의 기준 날짜

    @Column(nullable = false)
    private int amount;  // 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // 카테고리

    private Budget(LocalDate date, int amount, User user, Category category) {
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.category = category;
    }

    // 생성 메서드
    public static Budget createBudget(LocalDate date, int amount, User user, Category category) {
        return new Budget(
            date,
            amount,
            user,
            category
        );
    }
}
