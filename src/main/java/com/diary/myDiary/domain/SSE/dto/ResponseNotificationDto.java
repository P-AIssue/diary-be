package com.diary.myDiary.domain.SSE.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Schema(description = "알림 Dto")
@Getter
@Setter
public class ResponseNotificationDto {

    private Long id;
    private String content;
    private String url;
    private Boolean isRead;

    @Builder
    public ResponseNotificationDto(Long id, String content, String url, Boolean isRead) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
    }
}