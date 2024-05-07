package com.wealdy.saemsembackend.domain.spending.controller.response;

import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import com.wealdy.saemsembackend.domain.spending.controller.dto.SpendingSummaryDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SpendingListResponse {

    private final long spendingTotal;
    private final List<SpendingSummaryDto> spendingTotalByCategory;
    private final ListResponseDto<SpendingResponse> spendingList;

    public static SpendingListResponse of(
        long spendingTotal, List<SpendingSummaryDto> spendingTotalByCategory, ListResponseDto<SpendingResponse> spendingList
    ) {
        return new SpendingListResponse(spendingTotal, spendingTotalByCategory, spendingList);
    }
}
