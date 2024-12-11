package com.diary.myDiary.domain.notification.entity;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    // NotificationId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    // Member 와 1 : n 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 알림 type 고정 값 사용
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // 메시지
    @Column(nullable = false)
    private String message;

    // 읽음을 확인 (true, false)
    @Setter
    @Column(nullable = false)
    private boolean isRead;

    public static Notification from(Member member, NotificationType notificationType, String message, boolean isRead) {
        return Notification.builder()
                .member(member)
                .notificationType(notificationType)
                .message(message)
                .isRead(false)
                .build();
    }
}
