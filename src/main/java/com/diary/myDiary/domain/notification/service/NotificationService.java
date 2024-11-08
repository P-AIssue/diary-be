package com.diary.myDiary.domain.notification.service;

import com.diary.myDiary.domain.notification.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    // 알림 보내기
    void sendNotification(NotificationDto notificationDto);

    // 알림 리스트
    List<NotificationDto> getNotification(Long memberId);

    // 알림 읽기 후 리다이렉트
    String readNotification(Long notificationId);

    // 알림 전체 읽기
    void readAllNotification(Long memberId);

    // 알림 삭제하기
    void deleteNotification(Long notificationId);

    // 알림 전체 삭제하기
    void deleteAllNotification(Long memberId);
}
