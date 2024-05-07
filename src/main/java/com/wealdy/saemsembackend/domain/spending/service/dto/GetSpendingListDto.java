package com.wealdy.saemsembackend.domain.spending.service.dto;

import com.wealdy.saemsembackend.domain.core.response.ListResponseDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingListDto {

    private final long spendingTotal;
    private final List<GetSpendingSummaryDto> spendingTotalByCategory;
    private final ListResponseDto<GetSpendingDto> spendingList;

    public static GetSpendingListDto of(
        long spendingTotal, List<GetSpendingSummaryDto> spendingTotalByCategory, ListResponseDto<GetSpendingDto> spendingList
    ) {
        return new GetSpendingListDto(spendingTotal, spendingTotalByCategory, spendingList);
    }
}
