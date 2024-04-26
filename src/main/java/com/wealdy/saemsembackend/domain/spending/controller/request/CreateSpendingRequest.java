package com.wealdy.saemsembackend.domain.spending.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateSpendingRequest {

    @NotBlank(message = "카테고리는 필수 값입니다.")
    private String categoryName;

    @NotBlank(message = "날짜는 필수 값입니다.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "날짜 형식은 'yyyy-MM-dd'T'HH:mm:ss' 으로 입력해야 합니다.")
    private String date;

    @PositiveOrZero(message = "금액은 0원 이상으로 입력해야 합니다.")
    private long amount;

    @Size(max = 200, message = "메모는 200자까지 입력할 수 있습니다.")
    private String memo;

    private boolean excludeTotal;
}
