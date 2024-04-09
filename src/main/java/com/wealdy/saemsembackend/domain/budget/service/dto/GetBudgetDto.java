package com.wealdy.saemsembackend.domain.budget.service.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetBudgetDto {

    private String category;
    private int amount;
}
