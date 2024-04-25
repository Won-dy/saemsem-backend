package com.wealdy.saemsembackend.domain.budget.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.budget.controller.request.CreateBudgetRequest;
import com.wealdy.saemsembackend.domain.budget.controller.response.BudgetListResponse;
import com.wealdy.saemsembackend.domain.budget.service.BudgetService;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.core.util.DateUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
@Validated
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public Response<Void> createBudget(@Valid @RequestBody CreateBudgetRequest request, @RequestAttribute(name = USER_ID_KEY) String userId) {
        budgetService.createBudget(DateUtils.convertFirstDayOfMonth(request.getYear(), request.getMonth()), request.getBudgets(), userId);
        return Response.OK;
    }

    @GetMapping
    public Response<BudgetListResponse> getBudgetList(
        @RequestParam @Positive(message = "년도는 0 이상으로 입력해야 합니다.") int year,
        @RequestParam @Range(min = 1, max = 12, message = "월은 1~12 범위 내에서 입력해야 합니다.") int month,
        @RequestAttribute(name = USER_ID_KEY) String userId
    ) {
        LocalDate localDate = DateUtils.convertFirstDayOfMonth(year, month);
        return Response.of(BudgetListResponse.of(localDate, budgetService.getBudgetList(localDate, userId)));
    }
}
