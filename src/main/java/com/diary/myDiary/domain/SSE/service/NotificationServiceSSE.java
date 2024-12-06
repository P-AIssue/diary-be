package com.diary.myDiary.domain.SSE.service;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.SSE.entity.NotificationSSE;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationServiceSSE {

    // 알림 생성
    NotificationSSE createNotification(Member member, NotificationType notificationType, String content, String url);

    // 구독
    SseEmitter subscribe(Long memberId, String lastEventId);

    // 알림 보내기
    void send(Member member, NotificationType notificationType, String content, String url);


}
