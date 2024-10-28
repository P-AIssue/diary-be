package com.diary.myDiary.domain.member.controller;


import com.diary.myDiary.domain.member.dto.*;
import com.diary.myDiary.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    @Operation(summary = "회원 가입", description = "사용자가 회원가입을 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void singUp(@Valid @RequestBody MemberSignUpDTO memberSignUpDTO) throws Exception {
        memberService.signup(memberSignUpDTO);
    }

    /**
     * 회원정보수정
     */
    @PutMapping
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void updateInfo(@Valid @RequestBody MemberUpdateDTO memberUpdateDTO) throws Exception {
        memberService.update(memberUpdateDTO);
    }

    /**
     * 비밀번호수정
     */
    @PutMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "비밀번호를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) throws Exception {
        memberService.updatePassword(updatePasswordDTO.checkPassword(), updatePasswordDTO.newPassword());
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원탈퇴합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithdrawDTO memberWithdrawDTO)throws Exception {
        memberService.withdraw(memberWithdrawDTO.checkPassword());
    }

    /**
     * 회원정보조회
     */
    @GetMapping("/{id}")
    @Operation(summary = "회원 정보 조회", description = "회원의 정보를 조회합니다.")
    public ResponseEntity<MemberInfoDTO> getInfo(@Valid @PathVariable("id") Long id) throws Exception {
        MemberInfoDTO info = memberService.getInfo(id);
        return ResponseEntity.ok(info);
    }

    /**
     * 내정보조회
     */
    @GetMapping
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    public ResponseEntity<MemberInfoDTO> getMyInfo() throws Exception {
        MemberInfoDTO myInfo = memberService.getMyInfo();
        return ResponseEntity.ok(myInfo);
    }
}
