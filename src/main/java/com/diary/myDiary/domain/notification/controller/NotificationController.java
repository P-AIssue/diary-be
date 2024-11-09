package com.diary.myDiary.domain.notification.controller;

import com.diary.myDiary.domain.notification.dto.NotificationDto;
import com.diary.myDiary.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 보내기
    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationDto notificationDto) {
        notificationService.sendNotification(notificationDto);
    }

    // 알림 리스트
    @GetMapping("/list")
    public List<NotificationDto> getNotification(@RequestParam Long memberId) {
        return notificationService.getNotification(memberId);
    }

    // 알림 읽기 및 감정분석결과 확인하기
    @GetMapping("/read/{id}")
    public RedirectView readNotification(@PathVariable Long id) {
        String redirectUrl = notificationService.readNotification(id);
        return new RedirectView(redirectUrl);
    }

    // 모두 읽음 처리 하기
    @GetMapping("/read-all")
    public void readAllNotification(Long memberId) {
        notificationService.readAllNotification(memberId);
    }

    // 알림 일부 삭제
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    // 알림 모두 삭제
    @PostMapping("/delete-all")
    @ResponseBody
    public void deleteAllNotification(Long memberId) {
        notificationService.deleteAllNotification(memberId);
    }
}
