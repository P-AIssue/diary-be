package com.diary.myDiary.domain.notification.repository;


import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.exception.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationRepositoryTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("알림 조회 성공 테스트")
    public void testFindByIdOrThrow_Success() {
        Long notificationId = 1L;
        Long memberId = 1L;

        Notification notification = Notification.builder()
                .id(1L)
                .member(Member.builder().id(memberId).build())
                .isRead(false)
                .message("Test Message 1")
                .build();

        // findById 메소드가 Optional로 반환되는 부분을 모킹
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        Notification result = notificationRepository.findByIdOrThrow(notificationId);

        assertNotNull(result);
        assertEquals(notificationId, result.getId());

        verify(notificationRepository, times(1)).findById(notificationId);
    }

    @Test
    @DisplayName("알림 조회 실패 테스트 (알림 없음)")
    public void testFindByIdOrThrow_Failure() {
        Long notificationId = 1L;

        // findById 메소드가 Optional.empty()를 반환하는 경우 모킹
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // 예외 발생 확인
        assertThrows(NotificationException.class, () -> notificationRepository.findByIdOrThrow(notificationId));

        verify(notificationRepository, times(1)).findById(notificationId);
    }

    @Test
    @DisplayName("Member ID로 알림 조회 성공 테스트")
    public void testFindByMemberIdOrThrow_Success() {
        Long memberId = 1L;

        Notification notification1 = Notification.builder()
                .id(1L)
                .member(Member.builder().id(memberId).build())
                .isRead(false)
                .message("Test Message 1")
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .member(Member.builder().id(memberId).build())
                .isRead(false)
                .message("Test Message 2")
                .build();

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // findByMemberId 메소드 모킹
        when(notificationRepository.findByMemberId(memberId)).thenReturn(notifications);

        List<Notification> result = notificationRepository.findByMemberIdOrThrow(memberId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(notificationRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    @DisplayName("Member ID로 알림 조회 실패 테스트 (알림 없음)")
    public void testFindByMemberIdOrThrow_Failure() {
        Long memberId = 1L;

        // findByMemberId 메소드가 빈 리스트를 반환하는 경우 모킹
        when(notificationRepository.findByMemberId(memberId)).thenReturn(List.of());

        // 예외 발생 확인
        assertThrows(NotificationException.class, () -> notificationRepository.findByMemberIdOrThrow(memberId));

        verify(notificationRepository, times(1)).findByMemberId(memberId);
    }
}
