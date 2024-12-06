package com.diary.myDiary.domain.SSE.entity;

import lombok.Getter;

@Getter
public enum NotificationTypeSSE {
    // 일기 작성 완료
    success("일기가 작성 완료되었습니다."),

    // 오늘 일기를 작성해보세요
    toDay("오늘 하루는 어떠셨나요? 하루의 마무리로 일기를 작성해보세요."),

    // 감정 분석 완료
    analyzeEmotion("감정 분석이 완료되었습니다.");

    private final String message;

    NotificationTypeSSE(String message) {
        this.message = message;
    }
}
