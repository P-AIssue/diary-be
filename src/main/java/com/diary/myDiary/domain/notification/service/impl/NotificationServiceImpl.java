package com.diary.myDiary.domain.notification.service.impl;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.dto.NotificationDto;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import com.diary.myDiary.domain.notification.service.NotificationService;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    // 알림 보내기
    @Override
    public void sendNotification(NotificationDto notificationDto) {
        Member member = memberRepository.findById(notificationDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("멤버 없음"));

        Notification notification = Notification.builder()
                .member(member)
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .message(notificationDto.getMessage())
                .is_read(false)
                .build();

        notificationRepository.save(notification);
    }

    // 알림 리스트
    @Override
    @Transactional
    public List<NotificationDto> getNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDto dto = new NotificationDto(
                    notification.getNotificationId(),
                    notification.getMessage(),
                    notification.getNotificationType(),
                    notification.getIs_read(),
                    notification.getCreatedAt()
            );
            notificationDtos.add(dto);
        }
        return notificationDtos;
    }

    // 알림 부분 읽기
    @Override
    @Transactional
    public String readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림 없음"));

        notification.setIs_read(true);
        notificationRepository.save(notification);

        // 이거 수정 해야함......
        return "해당 감정 분석 결과 url/" + notificationId;
    }

    // 알림 전체 읽기
    @Override
    @Transactional
    public void readAllNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);

        for (Notification notification : notifications) {
            notification.setIs_read(true);
        }

        notificationRepository.saveAll(notifications);  // 여러 알림을 한번에 저장
    }

    // 알림 삭제하기
    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("삭제할 알림이 없음"));

        notificationRepository.delete(notification);
    }

    // 알림 전체 삭제하기
    @Override
    @Transactional
    public void deleteAllNotification(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);

        notificationRepository.deleteAll(notifications);
    }


}
