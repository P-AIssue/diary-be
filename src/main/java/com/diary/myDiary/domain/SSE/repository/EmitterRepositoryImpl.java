package com.diary.myDiary.domain.SSE.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // 특정 조건에 맞는 항목들을 필터링하여 반환하는 기 (Map 각 항목들 -> MapEntry 객체로 변환)
    // Emitters = Map<String, SseEmitter> 타입의 자료구조
    // stream = 컬렉션이나 배열 같은 연속된 흐름 (collect)
    // filter = 스트림에서 특정 조건을 만족하는 항목만 걸름
    // collect = 스트림의 요소들을 다른 형태로 변환할 때 사용

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId,sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String emitterId, Object event) {
        eventCache.put(emitterId,event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);

    }

    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key,emitter) -> {
                    if(key.startsWith(memberId)){
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key,emitter) -> {
                    if(key.startsWith(memberId)){
                        eventCache.remove(key);
                    }
                }
        );

    }
}
