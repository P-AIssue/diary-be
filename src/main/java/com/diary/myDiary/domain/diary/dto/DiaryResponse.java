package com.diary.myDiary.domain.diary.dto;

import com.diary.myDiary.domain.diary.entity.Diary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record DiaryResponse(
        Long id,
        String content,
        String emotionTag,
        String imageUrl,
        String createdDate
) {
    public static DiaryResponse of(Diary diary) {
        String formattedDate = formatCreatedDate(diary.getCreatedDate());
        return new DiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getEmotionTag(),
                diary.getImageUrl(),
                formattedDate
        );
    }

    public static List<DiaryResponse> listOf(List<Diary> diaries) {
        return diaries.stream()
                .map(DiaryResponse::of)
                .collect(Collectors.toList());
    }

    public static String formatCreatedDate(LocalDate createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
