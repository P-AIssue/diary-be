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
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS) // 알림 타입 설정
                .message("Test Notification") // 테스트 메시지
                .isRead(false) // 읽음 상태 초기화
                .build();

        // Mock 설정: findByIdOrThrow 호출 시 notification 반환
        when(notificationRepository.findByIdOrThrow(notificationId)).thenReturn(notification);

        // When: 알림 읽기 서비스 호출
        NotificationResponse result = notificationService.readNotification(notificationId);

        // Then: 결과 검증
        assertThat(result).isNotNull(); // 결과 객체가 null이 아님
        assertThat(result.message()).isEqualTo("Test Notification"); // 메시지가 예상 값과 동일
        assertThat(result.isRead()).isTrue(); // 읽음 상태가 true로 변경되었는지 확인

        // Mock 호출 검증
        verify(notificationRepository, times(1)).findByIdOrThrow(notificationId); // findByIdOrThrow 호출 확인
        verify(notificationRepository, times(1)).save(notification); // save 호출 확인
    }

    @Test
    @DisplayName("모든 알림 읽기 테스트")
    public void testReadAllNotification() {
        // Given
        Long memberId = 1L;

        Notification notification1 = Notification.builder()
                .id(1L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .message(NotificationType.EMOTION_ANALYSIS.getMessage())
                .isRead(false)
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .member(Member.builder().id(memberId).build())
                .message("Test Notification 2")
                .isRead(false)
                .build();

        Notification notification3 = Notification.builder()
                .id(3L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .message("혼합: " + NotificationType.EMOTION_ANALYSIS.getMessage() + " | 추가 메시지")
                .isRead(false)
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);

        // Mock 설정
        when(notificationRepository.findByMemberIdOrThrow(memberId)).thenReturn(notifications);

        // When
        List<NotificationResponse> results = notificationService.readAllNotification(memberId);

        // Then
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(3);

        // 각 알림이 읽음 상태로 업데이트되었는지 확인
        for (Notification notification : notifications) {
            assertThat(notification.isRead()).isTrue();  // 읽음 상태가 true로 변경되었는지 확인
        }

        // 메서드 호출 여부 확인
        verify(notificationRepository, times(1)).findByMemberIdOrThrow(memberId);  // findByMemberIdOrThrow 호출 확인
        verify(notificationRepository, times(1)).saveAll(notifications);  // saveAll 호출 확인
    }

    @Test
    @DisplayName("단일 알림 삭제 테스트")
    public void testDeleteNotification() {
        // Given
        Long notificationId = 1L;
        Long memberId = 1L;

        Notification notification = Notification.builder()
                .id(notificationId)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS) // 알림 타입 설정
                .message("Test Notification") // 테스트 메시지
                .isRead(false) // 읽음 상태 초기화
                .build();

        // findByIdOrThrow를 사용하여 notification을 반환하도록 mock 설정
        when(notificationRepository.findByIdOrThrow(notificationId)).thenReturn(notification);

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        verify(notificationRepository, times(1)).findByIdOrThrow(notificationId);
        verify(notificationRepository, times(1)).delete(notification); // 삭제 메서드 호출 확인
    }

    @Test
    @DisplayName("모든 알림 삭제 테스트")
    public void testDeleteAllNotification() {
        // Given
        Long memberId = 1L;

        Notification notification1 = Notification.builder()
                .id(1L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .message(NotificationType.EMOTION_ANALYSIS.getMessage())
                .isRead(false)
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .member(Member.builder().id(memberId).build())
                .message("Test Notification 2")
                .isRead(false)
                .build();

        Notification notification3 = Notification.builder()
                .id(3L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .message("혼합: " + NotificationType.EMOTION_ANALYSIS.getMessage() + " | 추가 메시지")
                .isRead(false)
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);

        // Mock 설정
        when(notificationRepository.findByMemberIdOrThrow(memberId)).thenReturn(notifications);

        // When
        notificationService.deleteAllNotification(memberId);

        // Then
        verify(notificationRepository, times(1)).findByMemberIdOrThrow(memberId); // findByMemberIdOrThrow 호출 검증
        verify(notificationRepository, times(1)).deleteAll(notifications); // deleteAll 호출 검증
    }
}

