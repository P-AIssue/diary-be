package com.diary.myDiary.domain.admin.controller;

import com.diary.myDiary.domain.admin.service.AdminService;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.entity.Role;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 관리자 대시보드
    @GetMapping("/home")
    public String home() {
        return "admin/home";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 사용자 정보 조회
        Member member = adminService.getByUserName(username);

        if (member != null) {
            // 비밀번호 검증
            if (passwordEncoder.matches(password, member.getPassword())) {
                // 관리자 권한 확인
                if (Role.ADMIN.equals(member.getRole())) {
                    model.addAttribute("message", "로그인 성공!");
                    return "admin/memberList";
                } else {
                    model.addAttribute("error", "관리자만 접근할 수 있습니다.");
                    return "admin/home";
                }
            } else {
                model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
                return "admin/home";
            }
        } else {
            model.addAttribute("error", "아이디나 비밀번호가 잘못되었습니다.");
            return "admin/home";
        }
    }

    // 전체 멤버 리스트 조회
    @GetMapping("/memberList")
    public String getAllMembers(Model model) {
        model.addAttribute("members", adminService.getAllMembers());
        log.info(adminService.getAllMembers().toString());
        return "admin/memberList";
    }

    // 멤버 수정 화면
    @GetMapping("/members/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
        Member member = adminService.getMemberById(id);
        model.addAttribute("member", member);
        return "admin/updateMember";
    }

    // 멤버 수정
    @PostMapping("/members/update/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member) {
        adminService.updateMember(id, member);
        return "redirect:/admin/memberList";
    }

    // 멤버 삭제
    @PostMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        adminService.deleteMember(id);
        return "redirect:/admin/memberList";
    }
}
