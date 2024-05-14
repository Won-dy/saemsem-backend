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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private long amount;  // 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // 카테고리

    private Budget(LocalDate date, long amount, User user, Category category) {
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.category = category;
    }

    public void updateBudget(long amount) {
        this.amount = amount;
    }

    // 생성 메서드
    public static Budget createBudget(LocalDate date, long amount, User user, Category category) {
        return new Budget(
            date,
            amount,
            user,
            category
        );
    }

    public static Budget of(Long id, LocalDate date, long amount, User user, Category category) {
        return new Budget(
            id,
            date,
            amount,
            user,
            category
        );
    }
}
