package com.wealdy.saemsembackend.domain.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdResponseDto {

    private final long id;

    public static IdResponseDto from(long id) {
        return new IdResponseDto(id);
    }
}
