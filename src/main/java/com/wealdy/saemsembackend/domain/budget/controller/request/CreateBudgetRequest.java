package com.wealdy.saemsembackend.domain.budget.controller.request;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateBudgetRequest {

    @NotBlank(message = "날짜는 필수 값입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "날짜 형식은 'yyyy-MM' 으로 입력해야 합니다.")
    private String date;

    @NotEmpty(message = "예산은 필수 값입니다.")
    private List<@Valid GetBudgetDto> budgets;
}
