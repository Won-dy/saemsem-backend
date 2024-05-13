package com.wealdy.saemsembackend.domain.budget.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.LOGIN_ID_KEY;

import com.wealdy.saemsembackend.domain.budget.controller.request.CreateBudgetRequest;
import com.wealdy.saemsembackend.domain.budget.controller.response.BudgetListResponse;
import com.wealdy.saemsembackend.domain.budget.controller.response.BudgetRecommendResponse;
import com.wealdy.saemsembackend.domain.budget.service.BudgetService;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.core.util.DateUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    /**
     * 예산 설정 API
     */
    @PostMapping
    public Response<Void> createBudget(@Valid @RequestBody CreateBudgetRequest request, @RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        budgetService.createBudget(DateUtils.convertFirstDayOfMonth(request.getYear(), request.getMonth()), request.getBudgets(), loginId);
        return Response.OK;
    }

    /**
     * 예산 목록 조회 API
     */
    @GetMapping
    public Response<BudgetListResponse> getBudgetList(
        @RequestParam @Positive(message = "년도는 0 이상으로 입력해야 합니다.") int year,
        @RequestParam @Range(min = 1, max = 12, message = "월은 1~12 범위 내에서 입력해야 합니다.") int month,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        LocalDate localDate = DateUtils.convertFirstDayOfMonth(year, month);
        return Response.of(BudgetListResponse.of(localDate, budgetService.getBudgetList(localDate, loginId)));
    }

    /**
     * 예산 설계 (추천) API
     */
    @GetMapping("/recommend")
    public Response<BudgetRecommendResponse> recommendBudget(
        @RequestParam @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") long budgetTotal,
        @RequestParam @Positive(message = "년도는 0 이상으로 입력해야 합니다.") int year,
        @RequestParam @Range(min = 1, max = 12, message = "월은 1~12 범위 내에서 입력해야 합니다.") int month,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        LocalDate localDate = DateUtils.convertFirstDayOfMonth(year, month);
        return Response.of(BudgetRecommendResponse.of(localDate, budgetTotal, budgetService.recommendBudget(budgetTotal, localDate, loginId)));
    }
}
