package com.wealdy.saemsembackend.domain.budget.controller.request;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateBudgetRequest {

    // todo: String to Date 찾아보기, 유효성 검사 추가
    private LocalDate date;
    private List<GetBudgetDto> budgets;
}
