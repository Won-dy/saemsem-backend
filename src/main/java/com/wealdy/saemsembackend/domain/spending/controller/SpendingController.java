package com.wealdy.saemsembackend.domain.spending.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.LOGIN_ID_KEY;

import com.wealdy.saemsembackend.domain.core.response.IdResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.spending.controller.dto.SpendingSummaryDto;
import com.wealdy.saemsembackend.domain.spending.controller.request.CreateSpendingRequest;
import com.wealdy.saemsembackend.domain.spending.controller.request.UpdateExcludeRequest;
import com.wealdy.saemsembackend.domain.spending.controller.request.UpdateSpendingRequest;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingListResponse;
import com.wealdy.saemsembackend.domain.spending.controller.response.SpendingResponse;
import com.wealdy.saemsembackend.domain.spending.service.SpendingService;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingListDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingRecommendDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingStatisticsDto;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingTodayDto;
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

    /**
     * 지출 생성 API
     */
    @PostMapping
    public Response<IdResponseDto> createSpending(
        @Valid @RequestBody CreateSpendingRequest request,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        Long spendingId = spendingService.createSpending(
            LocalDateTime.parse(request.getDate()),
            request.getAmount(),
            request.getMemo(),
            request.isExcludeTotal(),
            request.getCategoryName(),
            loginId
        );

        return Response.of(IdResponseDto.from(spendingId));
    }

    /**
     * 지출 상세 조회 API
     */
    @GetMapping("/{spendingId}")
    public Response<SpendingResponse> getSpending(@PathVariable Long spendingId, @RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        return Response.of(SpendingResponse.from(spendingService.getSpending(spendingId, loginId)));
    }

    /**
     * 지출 목록 조회 API (with Java) java 로 처리 로직 구현
     */
    @GetMapping
    public Response<GetSpendingListDto> getSpendingList(
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String startDate,
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String endDate,
        @RequestParam(required = false) List<String> category,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long minAmount,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long maxAmount,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        return Response.of(spendingService.getSpendingList(startLocalDate, endLocalDate, category, minAmount, maxAmount, loginId));
    }

    /**
     * 지출 목록 조회 API (with Query) 쿼리로 모든 응답 값들을 조회
     */
    @GetMapping("/query")
    public Response<SpendingListResponse> getSpendingListWithQuery(
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String startDate,
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 'yyyy-MM-dd' 으로 입력해야 합니다.") String endDate,
        @RequestParam(required = false) List<String> category,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long minAmount,
        @RequestParam(required = false) @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.") Long maxAmount,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        ListResponseDto<SpendingResponse> spendingList = ListResponseDto.from(
            spendingService.getSpendingListWithQuery(startLocalDate, endLocalDate, category, minAmount, maxAmount, loginId).stream()
                .map(SpendingResponse::from)
                .toList());
        long sumOfAmount = spendingService.getSumOfAmountByDate(startLocalDate, endLocalDate, category, minAmount, maxAmount, loginId);
        final List<SpendingSummaryDto> sumOfAmountByCategory =
            spendingService.getSumOfAmountByCategory(startLocalDate, endLocalDate, category, minAmount, maxAmount, loginId).stream()
                .map(SpendingSummaryDto::from)
                .toList();

        return Response.of(SpendingListResponse.of(sumOfAmount, sumOfAmountByCategory, spendingList));
    }

    /**
     * 지출 수정 API
     */
    @PutMapping("/{spendingId}")
    public Response<IdResponseDto> update(
        @PathVariable Long spendingId,
        @Valid @RequestBody UpdateSpendingRequest request,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        spendingService.updateSpending(
            spendingId,
            LocalDateTime.parse(request.getDate()),
            request.getAmount(),
            request.getMemo(),
            request.getCategoryName(),
            loginId
        );

        return Response.of(IdResponseDto.from(spendingId));
    }

    /**
     * 오늘 지출 추천 API
     */
    @GetMapping("/recommend")
    public Response<GetSpendingRecommendDto> recommendSpending(@RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        return Response.of(spendingService.recommendSpending(loginId));
    }

    /**
     * 오늘 지출 안내 API
     */
    @GetMapping("/today")
    public Response<GetSpendingTodayDto> spendingToday(@RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        return Response.of(spendingService.spendingToday(loginId));
    }

    /**
     * 지출 통계 API
     */
    @GetMapping("/statistics")
    public Response<GetSpendingStatisticsDto> spendingStatistics(@RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        return Response.of(spendingService.spendingStatistics(loginId));
    }

    /**
     * 지출의 합계 제외 수정 API
     */
    @PutMapping("/exclude/{spendingId}")
    public Response<IdResponseDto> updateExclude(
        @PathVariable Long spendingId,
        @Valid @RequestBody UpdateExcludeRequest request,
        @RequestAttribute(name = LOGIN_ID_KEY) String loginId
    ) {
        spendingService.updateExclude(spendingId, request.getExcludeTotal(), loginId);

        return Response.of(IdResponseDto.from(spendingId));
    }

    /**
     * 지출 삭제 API
     */
    @DeleteMapping("/{spendingId}")
    public Response<Void> deleteSpending(@PathVariable Long spendingId, @RequestAttribute(name = LOGIN_ID_KEY) String loginId) {
        spendingService.deleteSpending(spendingId, loginId);
        return Response.OK;
    }
}
