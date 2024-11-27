package com.diary.myDiary.domain.gpt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmotionAnalyzeDTO {

    private String model;
    private List<Map<String, String>> messages;

    @Builder
    public EmotionAnalyzeDTO(String model, List<Map<String, String>> messages) {
        this.model = (model != null) ? model : "gpt-4";
        this.messages = messages;
    }
}
