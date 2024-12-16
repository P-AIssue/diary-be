package com.diary.myDiary.domain.gpt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;


public record EmotionAnalyzeDTO(
            String model,
            List<Map<String, String>> messages
){}
