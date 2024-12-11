package com.diary.myDiary.domain.admin.service;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;

import java.util.List;

public interface AdminService {
    // 모든 멤버 조회
    List<MemberInfoDTO> getAllMembers();

    // 특정 멤버 조회
    MemberInfoDTO getMemberById(Long id);

    // 멤버 수정
    void updateMember(Long id, MemberInfoDTO memberDto);

    // 멤버 삭제
    void deleteMember(Long id);

}



