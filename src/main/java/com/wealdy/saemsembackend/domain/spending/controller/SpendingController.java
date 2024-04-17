package com.wealdy.saemsembackend.domain.spending.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.response.IdResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.spending.controller.dto.SpendingSummaryDto;
import com.wealdy.saemsembackend.domain.spending.controller.request.CreateSpendingRequest;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingListResponse;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingResponse;
import com.wealdy.saemsembackend.domain.spending.service.SpendingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spendings")
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping
    public Response<IdResponseDto> create(@Valid @RequestBody CreateSpendingRequest request, HttpServletRequest httpRequest) {
        String userId = String.valueOf(httpRequest.getAttribute(USER_ID_KEY));
        Long spendingId = spendingService.createSpending(
            request.getDate(), request.getAmount(), request.getMemo(), request.isExcludeTotal(), request.getCategoryName(), userId);

        return Response.of(IdResponseDto.from(spendingId));
    }

    @GetMapping("/{spendingId}")
    public Response<SpendingResponse> getSpending(@PathVariable Long spendingId) {
        return Response.of(SpendingResponse.from(spendingService.getSpending(spendingId)));
    }

    @GetMapping
    public Response<SpendingListResponse> getSpendingList(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @RequestParam(required = false) List<String> category,
        @RequestParam(required = false) Long minAmount,
        @RequestParam(required = false) Long maxAmount
    ) {
        ListResponseDto<SpendingResponse> spendingList = ListResponseDto.from(
            spendingService.getSpendingList(startDate, endDate, category, minAmount, maxAmount).stream()
                .map(SpendingResponse::from)
                .toList());
        long sumOfAmount = spendingService.getSumOfAmountByDate(startDate, endDate);
        List<SpendingSummaryDto> sumOfAmountByCategory = spendingService.getSumOfAmountByCategory(startDate, endDate).stream()
            .map(SpendingSummaryDto::from)
            .toList();

        return Response.of(SpendingListResponse.of(sumOfAmount, sumOfAmountByCategory, spendingList));
    }

    @DeleteMapping("/{spendingId}")
    public Response<Void> deleteSpending(@PathVariable Long spendingId) {
        spendingService.deleteSpending(spendingId);
        return Response.OK;
    }
}
