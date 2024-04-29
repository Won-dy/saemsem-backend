package com.wealdy.saemsembackend.domain.spending.repository;

import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import com.wealdy.saemsembackend.domain.spending.repository.projection.SpendingSummaryProjection;
import com.wealdy.saemsembackend.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpendingRepository extends JpaRepository<Spending, Long>, JpaSpecificationExecutor<Spending> {

    Optional<Spending> findByIdAndUser(Long id, User user);

    @Query(value = "select s from Spending s join fetch s.category "
        + "where s.date between :startDate and :endDate and s.user = :user "
        + "and (:minAmount is null or s.amount >= :minAmount) "
        + "and (:maxAmount is null or s.amount <= :maxAmount) "
        + "and (:category is null or s.category.name in :category) "
        + "order by s.date desc")
    List<Spending> findByDateBetweenOrderByDateDesc(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("category") List<String> category,
        @Param("minAmount") Long minAmount,
        @Param("maxAmount") Long maxAmount,
        @Param("user") User user
    );

    @Query(value = "select case when sum(s.amount) is null then 0 else sum(s.amount) end "
        + "from Spending s where s.excludeTotal = 'N' and s.date between :startDate and :endDate and s.user = :user "
        + "and (:minAmount is null or s.amount >= :minAmount) "
        + "and (:maxAmount is null or s.amount <= :maxAmount) "
        + "and (:category is null or s.category.name in :category)")
    long getSumOfAmountByDate(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("category") List<String> category,
        @Param("minAmount") Long minAmount,
        @Param("maxAmount") Long maxAmount,
        @Param("user") User user
    );

    @Query(nativeQuery = true,
        value = "select categoryName, sum(amount) as amount "
            + "from (select c.name as categoryName, sum(s.amount) as amount from spending s "
            + "left join category c on c.category_id = s.category_id "
            + "where s.exclude_total='N' and s.date between :startDate and :endDate and s.user_id = :userId "
            + "and (:minAmount is null or s.amount >= :minAmount) "
            + "and (:maxAmount is null or s.amount <= :maxAmount) "
            + "and (:categorySize = 0 or c.name in (:category)) "
            + "group by c.name "
            + "union "
            + "select c.name as categoryName, 0 as amount from spending s "
            + "right join category c on c.category_id = s.category_id) "
            + "as spendingByCategory group by categoryName order by amount desc"
    )
    List<SpendingSummaryProjection> getSumOfAmountByCategory(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("categorySize") int categorySize,
        @Param("category") List<String> category,
        @Param("minAmount") Long minAmount,
        @Param("maxAmount") Long maxAmount,
        @Param("userId") String userId
    );
}
