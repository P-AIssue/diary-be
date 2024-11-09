package com.diary.myDiary.domain.notification.dto;


import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import java.util.List;
import java.util.stream.Collectors;

public record NotificationResponse (
        Long id,
        String message,
        NotificationType notificationType,
        Boolean isRead

){
    public static NotificationResponse of(Notification notification) {
        return new NotificationResponse(
                notification.getId(), notification.getMessage(), notification.getNotificationType(),
                notification.getIsRead());
    }

    public static List<NotificationResponse> listOf(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
    }
}
