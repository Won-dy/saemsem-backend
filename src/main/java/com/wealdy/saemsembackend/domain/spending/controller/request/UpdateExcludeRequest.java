package com.wealdy.saemsembackend.domain.spending.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateExcludeRequest {

    @NotNull(message = "합계 제외 여부는 필수 값입니다.")
    private Boolean excludeTotal;
}
