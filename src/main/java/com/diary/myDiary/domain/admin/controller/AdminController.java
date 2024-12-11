package com.diary.myDiary.domain.admin.controller;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;
import com.diary.myDiary.domain.admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 전체 멤버 리스트 조회
    @GetMapping("/members")
    public String getAllMembers(Model model) {
        model.addAttribute("members", adminService.getAllMembers());
        return "admin/memberList";
    }

    // 멤버 수정 화면
    @GetMapping("/members/{id}/edit")
    public String editMemberForm(@PathVariable Long id, Model model) {
        MemberInfoDTO memberDto = adminService.getMemberById(id);
        model.addAttribute("member", memberDto);
        return "admin/editMember";
    }

    // 멤버 수정
    @PostMapping("/members/{id}/edit")
    public String updateMember(@PathVariable Long id, @ModelAttribute MemberInfoDTO memberDto) {
        adminService.updateMember(id, memberDto);
        return "redirect:/admin/members";
    }

    // 멤버 삭제
    @PostMapping("/members/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        adminService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
