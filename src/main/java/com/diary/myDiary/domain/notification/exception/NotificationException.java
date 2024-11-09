package com.diary.myDiary.domain.notification.exception;

import com.diary.myDiary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationException extends RuntimeException {

    private final ErrorCode errorCode;
}
