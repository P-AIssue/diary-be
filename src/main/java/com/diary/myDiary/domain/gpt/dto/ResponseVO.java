package com.diary.myDiary.domain.gpt.dto;

public record ResponseVO<T> (
        T data
) {}