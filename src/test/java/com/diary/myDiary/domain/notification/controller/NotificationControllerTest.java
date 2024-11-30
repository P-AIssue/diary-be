package com.diary.myDiary.domain.notification.controller;

import com.diary.myDiary.domain.notification.dto.NotificationResponse;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class NotificationControllerTest {

    @MockBean
    private MockMvc mockMvc;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("알림 생성 테스트")
    public void testSendNotification() throws Exception {
        Long memberId = 1L;
        String message = "Test Notification Message";

        doNothing().when(notificationService).sendNotification(memberId, message);

        mockMvc.perform(post("/send")
                        .param("id", String.valueOf(memberId))
                        .param("message", message))
                .andExpect(status().isOk());

        verify(notificationService, times(1)).sendNotification(memberId, message);
    }

    @Test
    @DisplayName("알림 목록 조회 테스트")
    public void testGetNotification() throws Exception {
        Long memberId = 1L;
        NotificationResponse response = new NotificationResponse(1L, "Test Notification", NotificationType.EMOTION_ANALYSIS, true);
        List<NotificationResponse> responses = Arrays.asList(response);

        when(notificationService.getNotification(memberId)).thenReturn(responses);

        mockMvc.perform(get("/list")
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));

        verify(notificationService, times(1)).getNotification(memberId);
    }

    @Test
    @DisplayName("알림 읽기 테스트")
    public void testReadNotification() throws Exception {
        Long notificationId = 1L;
        NotificationResponse response = new NotificationResponse(1L, "Test Notification", NotificationType.EMOTION_ANALYSIS, true);

        when(notificationService.readNotification(notificationId)).thenReturn(response);

        mockMvc.perform(get("/read/{id}", notificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test Notification"));

        verify(notificationService, times(1)).readNotification(notificationId);
    }

    @Test
    @DisplayName("알림 전체 읽기 테스트")
    public void testReadAllNotification() throws Exception {
        Long memberId = 1L;
        NotificationResponse response = new NotificationResponse(1L, "Test Notification", NotificationType.EMOTION_ANALYSIS, true);
        List<NotificationResponse> responses = Arrays.asList(response);

        when(notificationService.readAllNotification(memberId)).thenReturn(responses);

        mockMvc.perform(get("/read-all")
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));

        verify(notificationService, times(1)).readAllNotification(memberId);
    }

    @Test
    @DisplayName("알림 삭제 테스트")
    public void testDeleteNotification() throws Exception {
        Long notificationId = 1L;

        doNothing().when(notificationService).deleteNotification(notificationId);

        mockMvc.perform(post("/delete/{id}", notificationId))
                .andExpect(status().isOk());

        verify(notificationService, times(1)).deleteNotification(notificationId);
    }

    @Test
    @DisplayName("알림 전체 삭제 테스트")
    public void testDeleteAllNotification() throws Exception {
        Long memberId = 1L;

        doNothing().when(notificationService).deleteAllNotification(memberId);

        mockMvc.perform(post("/delete-all")
                        .param("memberId", String.valueOf(memberId)))
                .andExpect(status().isOk());

        verify(notificationService, times(1)).deleteAllNotification(memberId);
    }
}
