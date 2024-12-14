package com.diary.myDiary.domain.diary.dto;

import com.diary.myDiary.domain.diary.entity.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record DiaryResponse(
        Long id,
        String content,
        String emotionTag,
        String imageUrl,
        LocalDate createDate
) {
    public static DiaryResponse of(Diary diary) {
        return new DiaryResponse(diary.getId(), diary.getContent(), diary.getEmotionTag(), diary.getImageUrl(), diary.getCreatedDate());
    }

    public static List<DiaryResponse> listOf(List<Diary> diaries) {
        return diaries.stream()
                .map(DiaryResponse::of)
                .collect(Collectors.toList());
    }
}
