package com.diary.myDiary.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 사용자 */
    ALREADY_EXIST_MEMBER(00, "이미 존재하는 사용자입니다."),
    NOT_FOUND_MEMBER(00, "사용자를 찾을 수 없습니다."),
    MISMATCH_PASSWORD(00, "비밀번호가 일치하지 않습니다."),

    /* 다이어리 */
    NOT_FOUND_DIARY(00, "다이어리를 찾을 수 없습니다."),

    /* 공용 */
    NOT_FOUND_ACCESS_TOKEN(00, "토큰을 찾을 수 없습니다."),
    EXPIRED_ACCESS_TOKEN(403, "토큰의 유효시간이 만료되었습니다."),
    ACCESS_DENIED(00, "권한이 없습니다.");

    private final int errorCode;
    private final String message;

}
