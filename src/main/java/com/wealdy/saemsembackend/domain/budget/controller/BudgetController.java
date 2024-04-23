package com.wealdy.saemsembackend.domain.budget.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.budget.controller.request.CreateBudgetRequest;
import com.wealdy.saemsembackend.domain.budget.controller.response.BudgetListResponse;
import com.wealdy.saemsembackend.domain.budget.service.BudgetService;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.core.util.DateUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
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
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public Response<Void> createBudget(@Valid @RequestBody CreateBudgetRequest request, @RequestAttribute(name = USER_ID_KEY) String userId) {
        budgetService.createBudget(request.getDate(), request.getBudgets(), userId);
        return Response.OK;
    }

    @GetMapping
    public Response<BudgetListResponse> getBudgetList(
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "날짜 형식은 'yyyy-MM' 으로 입력해야 합니다.") String date,
        @RequestAttribute(name = USER_ID_KEY) String userId
    ) {
        LocalDate localDate = DateUtils.convertFirstDayOfMonth(date);
        return Response.of(BudgetListResponse.of(localDate, budgetService.getBudgetList(localDate, userId)));
    }
}
