package com.diary.myDiary.domain.notification.controller;

import com.diary.myDiary.domain.notification.dto.NotificationResponse;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.entity.NotificationType;
import com.diary.myDiary.domain.notification.exception.NotificationException;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import com.diary.myDiary.domain.notification.service.NotificationService;
import com.diary.myDiary.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    // 직렬화
    ObjectMapper objectMapper = new ObjectMapper();

    List<Notification> notifications = Arrays.asList();
    private Long id = 1L;
    private NotificationType notificationType = NotificationType.EMOTION_ANALYSIS;
    private String message = "테스트 진행";
    private boolean isRead = false;

    private void clear() {
        em.flush();
        em.clear();
    }

    /**
    1. 알림이 제대로 가는지
    2. 알림 목록 조회가 되는지
    3. 알림 읽기 (전체읽기) 후 읽은 표시가 되는지
    4. 알림 삭제가 제대로 작동하는지
    5. 알림이 없는 상태에서 알림 삭제 오류처리가 재대로 되는지
     **/

    @Test
    @DisplayName("알림 생성 테스트")
    public void testSendNotification() throws Exception {
        // given
        String sendData = objectMapper.writeValueAsString(new NotificationResponse
                (id, message, notificationType, isRead));

        // when
        mockMvc.perform(post("/notification/send")
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON) // JSON 형태 반환이ㅛㅇ
                        .content(sendData))
                .andExpect(status().isOk());

        // then
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationException(ErrorCode.NOT_FOUND_NOTIFICATION)
        );

        assertThat(notification.getMessage()).isEqualTo(message);
        assertThat(notification.getNotificationType()).isEqualTo(notificationType);
        assertThat(notification.isRead()).isEqualTo(isRead);

    }

    @Test
    @DisplayName("알림 목록 조회 테스트")
    public void testGetNotification() throws Exception {
        // given


        // when

        // then



    }

    @Test
    @DisplayName("알림 읽기 테스트")
    public void testReadNotification() throws Exception {
        // given

        // when

        // then



    }

    @Test
    @DisplayName("알림 전체 읽기 테스트")
    public void testReadAllNotification() throws Exception {
        // given

        // when

        // then



    }

    @Test
    @DisplayName("알림 삭제 테스트")
    public void testDeleteNotification() throws Exception {
        // given

        // when

        // then



    }

    @Test
    @DisplayName("알림 전체 삭제 테스트")
    public void testDeleteAllNotification() throws Exception {
        // given

        // when

        // then



    }
}
