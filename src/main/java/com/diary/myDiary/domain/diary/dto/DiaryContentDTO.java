package com.diary.myDiary.domain.diary.dto;

import lombok.Builder;

public record DiaryContentDTO(
        String content
) {
    @Builder
    public DiaryContentDTO(String content) {
        this.content = content;
    }
}
