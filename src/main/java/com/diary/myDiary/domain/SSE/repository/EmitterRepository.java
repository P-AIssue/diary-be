package com.diary.myDiary.domain.SSE.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository{
    // 사용자의 임의 id 와 SseEmitter 객체 저장
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    // 사용자의 이벤트 목록을 캐시에 저장
    void saveEventCache(String emitterId, Object event);

    // 사용자의 id 로 시작하는 데이터를 해당 map에 반환
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);

    // 사용자의 id 로 시작하는 캐시데이터를 해당 map에 반환
    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);

    // 사용자의 id를 사용해 해당 데이터를 map에서 삭제
    void deleteById(String emitterId);

    void deleteAllEmitterStartWithId(String memberId);

    void deleteAllEventCacheStartWithId(String memberId);
}
