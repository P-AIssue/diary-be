package com.diary.myDiary.domain.admin.service;

import com.diary.myDiary.domain.member.entity.Member;

import java.util.List;

public interface AdminService {
    // 모든 멤버 조회
    List<Member> getAllMembers();

    // Login
    Member getByUserName(String username);

    // 특정 멤버 조회
    Member getMemberById(Long id);

    // 멤버 수정
    void updateMember(Long id, Member member);

    // 멤버 삭제
    void deleteMember(Long id);

}



