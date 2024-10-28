package com.diary.myDiary.domain.emotionAnalysis.controller;

import com.diary.myDiary.domain.emotionAnalysis.dto.AnalyzeDto;
import com.diary.myDiary.domain.emotionAnalysis.service.EmotionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emotion-analysis")
@Slf4j
@RequiredArgsConstructor
public class EmotionAnalysisController {

    // private final EmotionAnalysisService emotionAnalysisService;

    // 감정 분석
    @PostMapping("/analyze")
    @Operation(summary = "감정분석", description = "일기에 따른 감정을 분석합니다.")
    public ResponseEntity analyzeEmotion(@Validated @RequestBody AnalyzeDto analyzeDto) throws Exception {


        return null;
    }
}
