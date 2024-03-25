package com.wealdy.saemsembackend.domain.core.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListResponseDto<T> {

    private long total;
    private List<T> list;

    public static <T> ListResponseDto<T> of(long total, List<T> list) {
        return new ListResponseDto<>(total, list);
    }

    public static <T> ListResponseDto<T> from(List<T> list) {
        return new ListResponseDto<>(list.size(), list);
    }
}
