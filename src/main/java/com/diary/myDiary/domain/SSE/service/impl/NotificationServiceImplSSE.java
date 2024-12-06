package com.diary.myDiary.domain.SSE.service.impl;

import com.diary.myDiary.domain.SSE.dto.ResponseNotificationDto;
import com.diary.myDiary.domain.SSE.entity.NotificationSSE;
import com.diary.myDiary.domain.SSE.repository.EmitterRepository;
import com.diary.myDiary.domain.SSE.repository.EmitterRepositoryImpl;
import com.diary.myDiary.domain.SSE.repository.NotificationSSERepository;
import com.diary.myDiary.domain.SSE.service.NotificationServiceSSE;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplSSE implements NotificationServiceSSE {

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationSSERepository notificationSSERepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    @Override
    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();

        // SseEmitter 객체 생성 (SSE 연결 위해)
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendToClient(emitter, emitterId, "EventStream Created. [memberId=" + memberId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }


    @Override
    public NotificationSSE createNotification(Member member, NotificationType notificationType, String content, String url) {
        return NotificationSSE.builder().member(member).notificationType(notificationType).content(content).url(url).build();
    }

    @Override
    public void send(Member member, NotificationType notificationType, String content, String url) {
        NotificationSSE notificationSSE = notificationSSERepository.save(createNotification(member, notificationType, content, url));
        String memberId = String.valueOf(member.getId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(memberId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notificationSSE);
                    sendToClient(emitter, key, ResponseNotificationDto.builder());
                }
        );
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data){
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }
}