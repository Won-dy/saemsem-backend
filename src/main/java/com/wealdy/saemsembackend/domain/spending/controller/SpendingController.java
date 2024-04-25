package com.wealdy.saemsembackend.domain.spending.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.response.IdResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.spending.controller.dto.SpendingSummaryDto;
import com.wealdy.saemsembackend.domain.spending.controller.request.SaveSpendingRequest;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingListResponse;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingResponse;
import com.wealdy.saemsembackend.domain.spending.service.SpendingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spendings")
@Validated
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping
    public Response<IdResponseDto> createSpending(@Valid @RequestBody SaveSpendingRequest request, @RequestAttribute(name = USER_ID_KEY) String userId) {
        Long spendingId = spendingService.createSpending(
            LocalDateTime.parse(request.getDate()),
            request.getAmount(),
            request.getMemo(),
            request.isExcludeTotal(),
            request.getCategoryName(),
            userId
        );

        return Response.of(IdResponseDto.from(spendingId));
    }

    @GetMapping("/{spendingId}")
    public Response<SpendingResponse> getSpending(@PathVariable Long spendingId, @RequestAttribute(name = USER_ID_KEY) String userId) {
        return Response.of(SpendingResponse.from(spendingService.getSpending(spendingId, userId)));
    }

    @GetMapping
    public Response<SpendingListResponse> getSpendingList(
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String startDate,
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String endDate,
        @RequestParam(required = false) List<String> category,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long minAmount,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long maxAmount,
        @RequestAttribute(name = USER_ID_KEY) String userId
    ) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        ListResponseDto<SpendingResponse> spendingList = ListResponseDto.from(
            spendingService.getSpendingList(startLocalDate, endLocalDate, category, minAmount, maxAmount, userId).stream()
                .map(SpendingResponse::from)
                .toList());
        long sumOfAmount = spendingService.getSumOfAmountByDate(startLocalDate, endLocalDate, category, minAmount, maxAmount, userId);
        List<SpendingSummaryDto> sumOfAmountByCategory =
            spendingService.getSumOfAmountByCategory(startLocalDate, endLocalDate, category, minAmount, maxAmount, userId).stream()
                .map(SpendingSummaryDto::from)
                .toList();

        return Response.of(SpendingListResponse.of(sumOfAmount, sumOfAmountByCategory, spendingList));
    }

    @PutMapping("/{spendingId}")
    public Response<IdResponseDto> update(
        @PathVariable Long spendingId,
        @Valid @RequestBody SaveSpendingRequest request,
        @RequestAttribute(name = USER_ID_KEY) String userId
    ) {
        spendingService.updateSpending(
            spendingId,
            LocalDateTime.parse(request.getDate()),
            request.getAmount(),
            request.getMemo(),
            request.isExcludeTotal(),
            request.getCategoryName(),
            userId
        );

        return Response.of(IdResponseDto.from(spendingId));
    }

    @DeleteMapping("/{spendingId}")
    public Response<Void> deleteSpending(@PathVariable Long spendingId, @RequestAttribute(name = USER_ID_KEY) String userId) {
        spendingService.deleteSpending(spendingId, userId);
        return Response.OK;
    }
}
