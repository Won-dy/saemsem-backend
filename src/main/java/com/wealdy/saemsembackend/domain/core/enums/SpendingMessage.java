package com.wealdy.saemsembackend.domain.core.enums;

import lombok.Getter;

@Getter
public enum SpendingMessage {

    SAVING_WELL("완벽한 절약 습관이네요 :) 오늘도 절약 도전!"),
    MODERATE_USAGE("적당히 사용하고 계시네요 :> 계속 유지해봐요!"),
    EXCEEDING_LIMIT("이대로 가다가는 예산이 부족해져요.. :< 절약하는 습관을 들여봐요~"),
    OVERSPENT("예산 초과 :( 돈이 줄줄 새요!!!");

    private final String message;

    SpendingMessage(String message) {
        this.message = message;
    }
}
