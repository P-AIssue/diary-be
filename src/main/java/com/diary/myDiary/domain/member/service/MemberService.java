package com.diary.myDiary.domain.member.service;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;
import com.diary.myDiary.domain.member.dto.MemberSignUpDTO;
import com.diary.myDiary.domain.member.dto.MemberUpdateDTO;

public interface MemberService {


    //== 회원가입 ==//
    void signup(MemberSignUpDTO memberSignUpDTO) throws Exception;

    //== 정보수정 ==//
    void updateNickname(MemberUpdateDTO memberUpdateDTO) throws Exception;

    void updatePassword(String checkPassword, String newPassword) throws Exception;

    //== 회원탈퇴 ==//
    void withdraw(String checkPassword) throws Exception;

    //== 정보조회 ==//
    MemberInfoDTO getInfo(Long id) throws Exception;

    MemberInfoDTO getMyInfo() throws Exception;

}
