package com.diary.myDiary.domain.SSE.entity;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@ToString(exclude = "member")
public class NotificationSSE extends BaseTimeEntity {

    // NotificationId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    // Member 와 1 : n 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 알림 type 고정 값 사용 (아이콘)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // 메시지
    @Embedded
    private NotificationContent content;

    // url
    @Embedded
    private RelatedUrl url;

    // 읽음 처리
    @Column(nullable = false)
    private Boolean isRead;


    @Builder
    public NotificationSSE(Member member, NotificationType notificationType, String content, String url) {
        this.member = member;
        this.notificationType = notificationType;
        this.content = new NotificationContent(content);
        this.url = new RelatedUrl(url);
        this.isRead = false;
    }

    public String getContent() {
        return content.getContent();
    }

    public String getUrl() {
        return url.getUrl();
    }

    public void read(){
        isRead = true;
    }
}