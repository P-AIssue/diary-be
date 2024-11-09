package com.diary.myDiary.domain.notification.dto;

import com.diary.myDiary.domain.notification.entity.NotificationType;
import java.time.LocalDateTime;

public record NotificationDto (
        Long id,
        Long memberId,
        String message,
        NotificationType notificationType,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public String getMessage() {
        return message;
    }

    public Long getMemberId() {
        return memberId;
    }
}


