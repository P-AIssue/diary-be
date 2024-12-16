package com.diary.myDiary.domain.gpt.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageGenerationRequestDTO {

    private String model;
    private String prompt;
    private int n;
    private String size;

    @Builder
    public ImageGenerationRequestDTO(String model, String prompt, int n, String size) {
        this.model = model;
        this.prompt = prompt;
        this.n = n;
        this.size = size;
    }
}
