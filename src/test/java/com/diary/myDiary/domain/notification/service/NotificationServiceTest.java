package com.diary.myDiary.domain.notification.service;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.domain.notification.dto.NotificationResponse;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private MemberRepository memberRepository;


    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .id(1L)
                .name("Test Member")
                .build();
        memberRepository.save(member);
    }


    @Test
    @DisplayName("알림 전송 확인")
    public void testSendNotification() {
        // Given
        Long memberId = 1L;
        String message = "Test Notification";

        // Mock 설정: findByMemberIdOrThrow 호출 시 가짜 Member 객체 반환
        Member mockMember = Member.builder().id(memberId).name("Test Member").build();
        when(memberRepository.findByMemberIdOrThrow(memberId)).thenReturn(mockMember);

        // When
        notificationService.sendNotification(memberId, message);

        // Then
        verify(memberRepository, times(1)).findByMemberIdOrThrow(memberId); // 호출 확인

    }


    // List<NotificationResponse>
    @Test
    @DisplayName("알림 조회 확인 (Enum, Entity, 혼합 사용)")
    public void testGetNotifications() {
        // Given
        Long memberId = 1L;

            // Enum 사용 엔티티
        Notification notification1 = Notification.builder()
                .id(1L) // id 설정
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS) // Enum 사용
                .message(NotificationType.EMOTION_ANALYSIS.getMessage()) // 메시지 설정
                .isRead(false)
                .build();

            // 일반 엔티티
        Notification notification2 = Notification.builder()
                .id(2L)
                .member(Member.builder().id(memberId).build())
                .message("Test Notification 2") // 커스텀 메시지
                .isRead(false)
                .build();

            // 혼합 사용 엔티티
        Notification notification3 = Notification.builder()
                .id(3L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS) // Enum 사용
                .message("혼합: " + NotificationType.EMOTION_ANALYSIS.getMessage() + " | 추가 메시지") // Enum + 커스텀 메시지
                .isRead(false)
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);

        // Mock 설정: findByMemberId 호출 시 mock 알림 리스트 반환
        when(notificationRepository.findByMemberId(memberId)).thenReturn(notifications);

        // When
        List<NotificationResponse> result = notificationService.getNotification(memberId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3); // 리스트 크기가 3인지 확인
        assertThat(result.get(0).message()).isEqualTo(NotificationType.EMOTION_ANALYSIS.getMessage()); // 첫 번째 메시지 확인
        assertThat(result.get(1).message()).isEqualTo("Test Notification 2"); // 두 번째 메시지 확인
        assertThat(result.get(2).message()).isEqualTo("혼합: 감정 분석 결과가 나왔습니다. | 추가 메시지"); // 세 번째 메시지 확인

        // Repository 메서드 호출 확인
        verify(notificationRepository, times(1)).findByMemberId(memberId);
    }


    @Test
    @DisplayName("단일 알림 읽기 테스트")
    public void testReadNotification() {
        // Given
        Long memberId = 1L;
        Long notificationId = 1L;

        Notification notification = Notification.builder()
                .id(notificationId)
                .isRead(false)
                .message("Test Message")
                .build();

        // findById가 호출되면 notification을 반환하도록 mock 설정
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.ofNullable(notification));

        // When
        NotificationResponse result = notificationService.readNotification(notificationId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.message()).isEqualTo("Test Message");
        assertThat(result.isRead()).isTrue();

        verify(notificationRepository, times(1)).findById(notificationId);
        verify(notificationRepository, times(1)).save(notification);
    }




    @Test
    @DisplayName("모든 알림 읽기 테스트")
    public void testReadAllNotification() {
        // Given
        Long memberId = 1L;

        List<Notification> notifications = Arrays.asList(
                Notification.builder().id(1L).isRead(false).message("Message 1").build(),
                Notification.builder().id(2L).isRead(false).message("Message 2").build()
        );

        when(notificationRepository.findByMemberId(memberId)).thenReturn(notifications);

        // When
        List<NotificationResponse> results = notificationService.readAllNotification(memberId);

        // Then
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);
        for (Notification notification : notifications) {
            assertThat(notification.isRead()).isTrue();
        }

        verify(notificationRepository, times(1)).findByMemberId(memberId);
        verify(notificationRepository, times(1)).saveAll(notifications);
    }


    @Test
    @DisplayName("단일 알림 삭제 테스트")
    public void testDeleteNotification() {
        // Given
        Long notificationId = 1L;
        Notification notification = Notification.builder().id(notificationId).build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        verify(notificationRepository, times(1)).findById(notificationId);
        verify(notificationRepository, times(1)).delete(notification);
    }


    @Test
    @DisplayName("모든 알림 삭제 테스트")
    public void testDeleteAllNotification() {
        // Given
        Long memberId = 1L;
        List<Notification> notifications = Arrays.asList(
                Notification.builder().id(1L).build(),
                Notification.builder().id(2L).build()
        );

        when(notificationRepository.findByMemberId(memberId)).thenReturn(notifications);

        // When
        notificationService.deleteAllNotification(memberId);

        // Then
        verify(notificationRepository, times(1)).findByMemberId(memberId);
        verify(notificationRepository, times(1)).deleteAll(notifications);
    }


}

