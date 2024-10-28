package com.diary.myDiary.domain.imageGeneration.repository;

import com.diary.myDiary.domain.imageGeneration.entity.ImageGeneration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageGenerationRepository extends JpaRepository<ImageGeneration, Long> {
}
