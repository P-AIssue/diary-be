package com.diary.myDiary.domain.diary.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record DiaryRequest(
        @NotNull(message = "내용을 입력해야 합니다.") String content,
        @Nullable String emotionTag
) {}
