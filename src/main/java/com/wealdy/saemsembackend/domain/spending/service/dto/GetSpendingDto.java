package com.wealdy.saemsembackend.domain.spending.service.dto;

import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import com.wealdy.saemsembackend.domain.spending.entity.Spending;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetSpendingDto {

    private final Long id;
    private final String categoryName;
    private final LocalDateTime date;
    private final long amount;
    private final String memo;
    private final YnColumn excludeTotal;

    public static GetSpendingDto from(Spending spending) {
        return new GetSpendingDto(
            spending.getId(),
            spending.getCategory().getName(),
            spending.getDate(),
            spending.getAmount(),
            spending.getMemo(),
            spending.getExcludeTotal()
        );
    }
}
