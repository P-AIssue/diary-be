package com.diary.myDiary.domain.SSE.controller;

import com.diary.myDiary.domain.SSE.service.NotificationServiceSSE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class NotificationSSEController {

    private final NotificationServiceSSE notificationServiceSSE;

    // 생성자 주입
    public NotificationSSEController(NotificationServiceSSE notificationServiceSSE) {
        this.notificationServiceSSE = notificationServiceSSE;
    }

    @Tag(name = "SSE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "2000", description = "SSE 연결 성공"),
            @ApiResponse(responseCode = "5000", description = "SSE 연결 실패")
    })
    @Operation(summary = "SSE 연결")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @AuthenticationPrincipal Principal principal,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        // 서비스에 사용자 ID와 마지막 이벤트 ID 전달
        return notificationServiceSSE.subscribe(Long.valueOf(principal.getName()), lastEventId);
    }
}
