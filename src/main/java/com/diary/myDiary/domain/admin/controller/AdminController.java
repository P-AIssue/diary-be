package com.diary.myDiary.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.diary.myDiary.domain.admin.service.AdminService;
import org.springframework.web.bind.annotation.*;

// 타임리프 연동 위해 RestController 미사용
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 관리자 대시보드
    @GetMapping("/home")
    public String home() {
        return "admin/home";
    }

    // 전체 멤버 리스트
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        model.addAttribute("members", adminService.getAllMembers());
        return "admin/memberList";
    }

    // 특정 멤버의 일기 리스트
    @GetMapping("/members/{memberId}/diaries")
    public String getMemberDiaries(@PathVariable Long memberId, Model model) {
        model.addAttribute("diaries", adminService.getMemberDiaries(memberId));
        return "admin/diaryList";
    }

    // 특정 멤버의 알림 리스트
    @GetMapping("/members/{memberId}/notifications")
    public String getMemberNotifications(@PathVariable Long memberId, Model model) {
        model.addAttribute("notifications", adminService.getMemberNotifications(memberId));
        return "admin/notificationList";
    }

    // 특정 일기의 상세 내용
    @GetMapping("/diaries/{diaryId}")
    public String getDiaryDetails(@PathVariable Long diaryId, Model model) {
        model.addAttribute("diary", adminService.getDiaryDetails(diaryId));
        return "admin/diaryDetails";
    }
}