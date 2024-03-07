package com.wealdy.saemsembackend.common.enums;

import lombok.Getter;

@Getter
public enum YnColumn {
    Y(true),
    N(false);

    private final boolean ynBoolean;

    YnColumn(boolean ynBoolean) {
        this.ynBoolean = ynBoolean;
    }
}
