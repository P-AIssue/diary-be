package com.diary.myDiary.domain.notification.controller;

import com.diary.myDiary.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @ResponseBody
    public void sendNotification(Long memberId, String message) {
        notificationService.sendNotification(memberId, message);
    }

    @GetMapping("/read/{id}")
    @ResponseBody
    public void readNotification(Long notificationId) {
        notificationService.readNotification(notificationId);
    }

    @GetMapping("/read-all")
    @ResponseBody
    public void readAllNotification(Long memberId) {
        notificationService.readAllNotification(memberId);
    }

    @PostMapping("/delete")
    @ResponseBody
    public void deleteNotification(Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
    @PostMapping("/delete-all")
    @ResponseBody
    public void deleteAllNotification(Long memberId) {
        notificationService.deleteAllNotification(memberId);
    }
}
