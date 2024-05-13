package com.wealdy.saemsembackend.domain.spending.repository.projection;

import com.wealdy.saemsembackend.domain.category.entity.Category;

public interface SpendingTodayProjection {

    Category getCategory();

    Long getUsedAmount();
}
