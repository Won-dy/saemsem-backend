package com.wealdy.saemsembackend.domain.spending.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateSpendingRequest {

    @NotBlank(message = "카테고리는 필수 값입니다.")
    private String categoryName;

    @NotNull(message = "날짜는 필수 값입니다.")
    private LocalDateTime date;

    @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.")
    private long amount;

    @Size(max = 200, message = "메모는 200자까지 입력할 수 있습니다.")
    private String memo;

    private boolean excludeTotal;
}
