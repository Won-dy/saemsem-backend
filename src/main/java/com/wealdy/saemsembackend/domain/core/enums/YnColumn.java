package com.wealdy.saemsembackend.domain.core.enums;

import lombok.Getter;

@Getter
public enum YnColumn {
    Y(true),
    N(false);

    private final boolean ynBoolean;

    YnColumn(boolean ynBoolean) {
        this.ynBoolean = ynBoolean;
    }

    public static YnColumn fromBoolean(boolean ynBoolean) {
        return ynBoolean ? Y : N;
    }
}
