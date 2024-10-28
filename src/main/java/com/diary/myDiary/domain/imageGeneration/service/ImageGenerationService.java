package com.diary.myDiary.domain.imageGeneration.service;

import com.diary.myDiary.domain.imageGeneration.dto.ImageGenerationDto;

public interface ImageGenerationService {
    // 이미지 생성
    ImageGenerationDto generateImage(ImageGenerationDto imageGenerationDto);
}
