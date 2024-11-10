package com.diary.myDiary.domain.notification.repository;

import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.exception.NotificationException;
import com.diary.myDiary.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Member의 ID를 기준으로 Notification 리스트 조회
    List<Notification> findByMemberId(Long memberId);

    // 알림이 없을 때 (부분)
    default Notification findByIdOrThrow(Long id) {
            return findById(id).orElseThrow(() -> new NotificationException(ErrorCode.NOT_FOUND_NOTIFICATION));
    }

    // 알림이 없을 때 (전체)
    default List<Notification> findByMemberIdOrThrow(Long memberId) {
        List<Notification> notifications = findByMemberId(memberId);
        if (notifications.isEmpty()) {
            throw new NotificationException(ErrorCode.NOT_FOUND_NOTIFICATION);
        }
        return notifications;
    }
}
