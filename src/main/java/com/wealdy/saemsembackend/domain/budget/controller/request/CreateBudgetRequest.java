package com.wealdy.saemsembackend.domain.budget.controller.request;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateBudgetRequest {

    @NotNull(message = "날짜는 필수 값입니다.")
    private LocalDate date;
    private List<@Valid GetBudgetDto> budgets;
}
