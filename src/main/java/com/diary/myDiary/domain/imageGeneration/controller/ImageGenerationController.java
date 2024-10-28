package com.diary.myDiary.domain.imageGeneration.controller;

import com.diary.myDiary.domain.imageGeneration.dto.ImageGenerationDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image-generation")
@RequiredArgsConstructor
@Slf4j
public class ImageGenerationController {

    // private final ImageGenerationService imageGenerationService;

    // 일기 분석 후 그림 생성
    @PostMapping
    @Operation(summary = "그림 생성", description = "일기 내용에 따라 그림을 생성합니다.")
    public ResponseEntity<ImageGenerationDto> generateImage(@Validated @RequestBody ImageGenerationDto imageGenerationDto) throws Exception {
        return null;
    }
}
