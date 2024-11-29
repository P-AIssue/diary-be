package com.diary.myDiary.global.exception;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "오류 처리", description = "각 서비스에 오류를 공통으로 처리합니다.")
@Controller
public class ErrorController {

    @Operation(summary = "접근 거부 처리", description = "접근 거부 처리")
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
