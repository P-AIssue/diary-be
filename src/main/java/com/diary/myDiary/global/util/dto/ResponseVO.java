package com.diary.myDiary.global.util.dto;

public record ResponseVO<T> (
        T data
) {}