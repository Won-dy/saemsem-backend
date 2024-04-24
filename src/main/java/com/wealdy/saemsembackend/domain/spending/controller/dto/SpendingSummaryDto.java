package com.wealdy.saemsembackend.domain.spending.controller.dto;

import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingSummaryDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SpendingSummaryDto {

    private final String categoryName;
    private final long amount;

    public static SpendingSummaryDto from(GetSpendingSummaryDto getSpendingSummaryDto) {
        return new SpendingSummaryDto(getSpendingSummaryDto.getCategoryName(), getSpendingSummaryDto.getAmount());
    }
}
