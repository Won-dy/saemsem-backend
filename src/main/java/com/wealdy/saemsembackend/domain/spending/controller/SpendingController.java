package com.wealdy.saemsembackend.domain.spending.controller;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.response.IdResponseDto;
import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.spending.controller.request.CreateSpendingRequest;
import com.wealdy.saemsembackend.domain.spending.service.SpendingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping("/spendings")
    public Response<IdResponseDto> create(@Valid @RequestBody CreateSpendingRequest request, HttpServletRequest httpRequest) {
        String userId = String.valueOf(httpRequest.getAttribute(USER_ID_KEY));
        Long spendingId = spendingService.createSpending(
            request.getDate(), request.getAmount(), request.getMemo(), request.isExcludeTotal(), request.getCategoryName(), userId);

        return Response.of(IdResponseDto.from(spendingId));
    }
}
