package com.diary.myDiary.domain.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {

    EMOTION_ANALYSIS("감정 분석 결과가 나왔습니다.")
    ;

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

}
