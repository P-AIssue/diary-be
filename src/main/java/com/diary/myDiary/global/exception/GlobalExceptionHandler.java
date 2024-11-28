package com.diary.myDiary.global.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.diary.myDiary.domain.member.exception.MemberException;
import com.diary.myDiary.domain.notification.exception.NotificationException;
import com.diary.myDiary.global.exception.dto.ErrorResponseV0;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ErrorResponseV0 handleMemberException(MemberException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return getErrorResponse(errorCode);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponseV0 handleTokenExpiredException(TokenExpiredException exception) {
        return ErrorResponseV0.builder()
                .name(ErrorCode.EXPIRED_ACCESS_TOKEN.name())
                .errorCode(ErrorCode.EXPIRED_ACCESS_TOKEN.getErrorCode())
                .message(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseV0 handleValidationException(MethodArgumentNotValidException exception) {
        return ErrorResponseV0.builder()
                .name("VALIDATION_ERROR")
                .errorCode(exception.getStatusCode().value())
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .build();
    }

    private ErrorResponseV0 getErrorResponse(ErrorCode errorCode) {
        return ErrorResponseV0.builder()
                .name(errorCode.name())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .build();
    }

}
