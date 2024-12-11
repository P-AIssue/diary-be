package com.diary.myDiary.domain.admin.controller;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;
import com.diary.myDiary.domain.admin.service.AdminService;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.entity.Role;
import com.diary.myDiary.domain.member.repository.MemberRepository;
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

    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {

        // 사용자 정보 조회
        Member member = adminService.getByUserName(username);

        if (member != null && member.getPassword().equals(password)) {
            // role이 admin인지 확인
            if (Role.ADMIN.equals(member.getRole())) {
                model.addAttribute("message", "로그인 성공!");
                return "admin/memberList";  // 대시보드로 리다이렉트
            } else {
                model.addAttribute("error", "관리자만 접근할 수 있습니다.");
                return "admin/home";  // 관리자 외의 사용자가 로그인 시 오류 메시지
            }
        } else {
            model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
            return "admin/home";  // 로그인 페이지로 돌아가기
        }
    }

    // 전체 멤버 리스트 조회
    @GetMapping("/memberList")
    public String getAllMembers(Model model) {
        model.addAttribute("members", adminService.getAllMembers());
        return "admin/memberList";
    }

    // 멤버 수정 화면
    @GetMapping("/updateMember/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
        MemberInfoDTO memberDto = adminService.getMemberById(id);
        model.addAttribute("member", memberDto);
        return "admin/updateMember";
    }

    // 멤버 수정
    @PostMapping("/members/{id}/edit")
    public String updateMember(@PathVariable Long id, @ModelAttribute MemberInfoDTO memberDto) {
        adminService.updateMember(id, memberDto);
        return "redirect:/admin/memberList";
    }

    // 멤버 삭제
    @PostMapping("/members/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        adminService.deleteMember(id);
        return "redirect:/admin/memberList";
    }
}
