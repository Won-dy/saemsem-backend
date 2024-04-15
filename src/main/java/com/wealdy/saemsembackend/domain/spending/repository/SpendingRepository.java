package com.wealdy.saemsembackend.domain.spending.repository;

import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendingRepository extends JpaRepository<Spending, Long> {

}
