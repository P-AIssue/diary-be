package com.diary.myDiary.domain.notification.controller;

import com.diary.myDiary.domain.notification.dto.NotificationResponse;
import com.diary.myDiary.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림", description = "알림 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 보내기
    @PostMapping("/send")
    @Operation(summary = "알림 생성", description = "알림을 보냅니다.")
    public void sendNotification(@RequestBody Long id, String message) {
        notificationService.sendNotification(id, message);
    }

    // 알림 리스트
    @GetMapping("/list")
    @Operation(summary = "알림 목록", description = "알림을 조회합니다.")
    public List<NotificationResponse> getNotification(@RequestParam Long memberId) {
        return notificationService.getNotification(memberId);
    }

    // 알림 읽기 및 감정분석결과 확인하기
    @GetMapping("/read/{id}")
    @Operation(summary = "알림 읽기", description = "해당 알림을 읽습니다.")
    public NotificationResponse readNotification(@PathVariable Long id) {
        return notificationService.readNotification(id);
    }

    // 모두 읽음 처리 하기
    @GetMapping("/read-all")
    @Operation(summary = "알림 전체 읽기", description = "모든 알림을 읽음처리합니다.")
    public List<NotificationResponse> readAllNotification(Long memberId) {
        return notificationService.readAllNotification(memberId);
    }

    // 알림 일부 삭제
    @PostMapping("/delete/{id}")
    @Operation(summary = "알림 삭제", description = "해당 알림을 삭제합니다.")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    // 알림 모두 삭제
    @PostMapping("/delete-all")
    @Operation(summary = "알림 전체 삭제", description = "모든 알림을 삭제합니다.")
    public void deleteAllNotification(Long memberId) {
        notificationService.deleteAllNotification(memberId);
    }
}
