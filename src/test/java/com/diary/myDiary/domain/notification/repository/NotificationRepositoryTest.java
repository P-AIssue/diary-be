package com.diary.myDiary.domain.notification.repository;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.notification.exception.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationRepositoryTest {

    @MockBean
    private NotificationRepository mockNotificationRepository;

    @Test
    @DisplayName("알림 조회 성공 테스트")
    public void testFindByIdOrThrow_Success() {
        Long notificationId = 1L;
        Long memberId = 1L;

        Notification notification = Notification.builder()
                .id(notificationId)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .isRead(false)
                .message("Test Notification")
                .build();

        // Mock 객체 설정
        when(mockNotificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // findByIdOrThrow 메서드 호출
        Notification result = mockNotificationRepository.findByIdOrThrow(notificationId);

        // 결과 검증
        // assertNotNull(result, "Result should not be null");
        assertEquals(notificationId, result.getId(), "Notification ID should match");

        // findById 호출 검증
        verify(mockNotificationRepository, times(1)).findByIdOrThrow(notificationId);
    }


    @Test
    @DisplayName("알림 조회 실패 테스트 (알림 없음)")
    public void testFindByIdOrThrow_Failure() {
        Long notificationId = 1L;

        // findById가 Optional.empty()를 반환하도록 모킹
        when(mockNotificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // 예외 발생을 기대하고 테스트
        assertThrows(NotificationException.class, () -> {
            mockNotificationRepository.findByIdOrThrow(notificationId);
        });

        // verify: findById가 1번 호출되었는지 확인
        verify(mockNotificationRepository, times(1)).findById(notificationId);
    }

    @Test
    @DisplayName("Member ID로 알림 조회 성공 테스트")
    public void testFindByMemberIdOrThrow_Success() {
        Long memberId = 1L;

        Notification notification1 = Notification.builder()
                .id(1L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .isRead(false)
                .message("Test Message 1")
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .member(Member.builder().id(memberId).build())
                .notificationType(NotificationType.EMOTION_ANALYSIS)
                .isRead(false)
                .message("Test Message 2")
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // findByMemberId 메소드 모킹
        Mockito.when(mockNotificationRepository.findByMemberId(memberId)).thenReturn(notifications);

        System.out.println(notifications);

        List<Notification> result = mockNotificationRepository.findByMemberIdOrThrow(memberId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        verify(mockNotificationRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    @DisplayName("Member ID로 알림 조회 실패 테스트 (알림 없음)")
    public void testFindByMemberIdOrThrow_Failure() {
        Long memberId = 1L;

        // findByMemberId 메소드가 빈 리스트를 반환하는 경우 모킹
        when(mockNotificationRepository.findByMemberId(memberId)).thenReturn(List.of());

        // 예외 발생 확인
        assertThrows(NotificationException.class, () -> mockNotificationRepository.findByMemberIdOrThrow(memberId));

        verify(mockNotificationRepository, times(1)).findByMemberId(memberId);
    }
}
