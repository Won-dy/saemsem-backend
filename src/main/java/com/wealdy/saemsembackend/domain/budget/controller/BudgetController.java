package com.wealdy.saemsembackend.domain.budget.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.budget.controller.request.CreateBudgetRequest;
import com.wealdy.saemsembackend.domain.budget.service.BudgetService;
import com.wealdy.saemsembackend.domain.core.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
