package com.wealdy.saemsembackend.domain.spending.controller.response;

import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.spending.service.dto.GetSpendingDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SpendingResponse {

    private final Long id;
    private final String categoryName;
    private final LocalDateTime date;
    private final long amount;
    private final String memo;
    private final YnColumn excludeTotal;

    public static SpendingResponse from(GetSpendingDto getSpendingDto) {
        return new SpendingResponse(
            getSpendingDto.getId(),
            getSpendingDto.getCategoryName(),
            getSpendingDto.getDate(),
            getSpendingDto.getAmount(),
            getSpendingDto.getMemo(),
            getSpendingDto.getExcludeTotal()
        );
    }
}
