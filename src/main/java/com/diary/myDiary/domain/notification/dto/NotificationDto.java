package com.diary.myDiary.domain.notification.dto;

import com.diary.myDiary.domain.notification.entity.NotificationType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
public class NotificationDto {
    private Long notificationId;
    private Long memberId;
    private String message;
    private NotificationType notificationType;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto(Long notificationId, String message, NotificationType notificationType, Boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

}

