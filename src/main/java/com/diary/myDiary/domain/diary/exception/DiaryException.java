package com.diary.myDiary.domain.diary.exception;

import com.diary.myDiary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DiaryException extends RuntimeException {

    private final ErrorCode errorCode;
}