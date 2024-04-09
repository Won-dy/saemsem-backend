package com.wealdy.saemsembackend.domain.budget.controller.request;

import com.wealdy.saemsembackend.domain.budget.service.dto.GetBudgetDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class CreateBudgetRequest {

    // todo: String to Date 찾아보기, 유효성 검사 추가
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    private List<GetBudgetDto> budgets;
}
