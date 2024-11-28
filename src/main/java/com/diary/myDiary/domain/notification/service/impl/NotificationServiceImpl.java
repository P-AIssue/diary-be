package com.diary.myDiary.domain.notification.service.impl;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.dto.NotificationResponse;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import com.diary.myDiary.domain.notification.service.NotificationService;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    // 이 부분이 아래 정적 팩토리 메서드 사용할려는데
    private NotificationType notificationType;
    private Boolean isRead;

    // 알림 보내기
    @Override
    @Transactional
    public void sendNotification(Long memberId, String message) {
        Member member = memberRepository.findByMemberIdOrThrow(memberId);

        Notification notification = Notification.from(member,notificationType, message, isRead);
        notificationRepository.save(notification);
    }

    // 알림 리스트
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        return NotificationResponse.listOf(notifications);
    }

    // 알림 부분 읽기
    @Override
    @Transactional
    public NotificationResponse readNotification(Long id) {
        Notification notification = notificationRepository.findByIdOrThrow(id);

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return NotificationResponse.of(notification);
    }

    // 알림 전체 읽기
    @Override
    @Transactional
    public List<NotificationResponse> readAllNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberIdOrThrow(memberId);

        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }

        notificationRepository.saveAll(notifications);  // 여러 알림을 한번에 저장

        return NotificationResponse.listOf(notifications);
    }

    // 알림 삭제하기
    @Override
    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findByIdOrThrow(id);
        notificationRepository.deleteById(notification.getId());
    }

    // 알림 전체 삭제하기
    @Override
    @Transactional
    public void deleteAllNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberIdOrThrow(memberId);
        notificationRepository.deleteAll(notifications);
    }
}