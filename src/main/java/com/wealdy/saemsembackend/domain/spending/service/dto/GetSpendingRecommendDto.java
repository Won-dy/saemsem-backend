package com.wealdy.saemsembackend.domain.spending.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingRecommendDto {

    private final long recommendSpendingTotal;
    private final List<GetSpendingRecommendByCategoryDto> recommendSpendingByCategory;

    public static GetSpendingRecommendDto of(long recommendSpendingTotal, List<GetSpendingRecommendByCategoryDto> recommendSpendingByCategory) {
        return new GetSpendingRecommendDto(recommendSpendingTotal, recommendSpendingByCategory);
    }
}
