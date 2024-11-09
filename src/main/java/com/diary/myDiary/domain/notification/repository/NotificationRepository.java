package com.diary.myDiary.domain.notification.repository;

import com.diary.myDiary.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Member의 ID를 기준으로 Notification 리스트 조회
    List<Notification> findByMemberId(Long memberId);

//    // 예외처리
//    default Notification getByIdOrThrow(Long id) {
//            return findById(id).orElseThrow(() -> new NotificationException(id.NOT_FOUND_NOTIFICATION));
//    }

    // 모든 데이터 삭제
    void deleteByMemberId(Long memberId);
}
