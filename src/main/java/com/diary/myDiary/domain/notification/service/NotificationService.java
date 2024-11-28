package com.diary.myDiary.domain.notification.service;

import com.diary.myDiary.domain.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    // 알림 보내기
    void sendNotification(Long memberId, String message);

    // 알림 리스트
    List<NotificationResponse> getNotification(Long memberId);

    // 알림 읽기
    NotificationResponse readNotification(Long id);

    // 알림 전체 읽기
    List<NotificationResponse> readAllNotification(Long memberId);

    // 알림 삭제하기
    void deleteNotification(Long notificationId);

    // 알림 전체 삭제하기
    void deleteAllNotification(Long memberId);
}
