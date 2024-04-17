package com.wealdy.saemsembackend.domain.spending.entity;

import com.wealdy.saemsembackend.domain.category.entity.Category;
import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Spending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spending_id")
    private Long id;  // 지출 id

    @Column(nullable = false)
    private LocalDateTime date;  // 지출일시

    @Column(nullable = false)
    private long amount;  // 금액

    @Column(length = 200)
    private String memo;  // 메모

    @ColumnDefault("'N'")
    @Column(nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private YnColumn excludeTotal;  // 지출 합계 제외 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // 카테고리

    private Spending(LocalDateTime date, long amount, String memo, boolean excludeTotal, User user, Category category) {
        this.date = date;
        this.amount = amount;
        this.memo = memo;
        this.excludeTotal = YnColumn.from(excludeTotal);
        this.user = user;
        this.category = category;
    }

    public static Spending createSpending(LocalDateTime date, long amount, String memo, boolean excludeTotal, User user, Category category) {
        return new Spending(
            date,
            amount,
            memo,
            excludeTotal,
            user,
            category
        );
    }
}
