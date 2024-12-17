package com.diary.myDiary.domain.admin.service.impl;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.domain.admin.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    public AdminServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 멤버 리스트
    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 멤버 이메일 가져오기
    @Override
    public Member getByUserName(String username) {
        return memberRepository.getByUsernameOrThrow(username);
    }

    // 멤버 아이디 가져오기
    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findByMemberIdOrThrow(id);
    }

    // 멤버 수정
    @Override
    public void updateMember(Long id, Member updatedMember) {
        // ID로 기존 멤버 찾기
        Member existingMember = memberRepository.findByMemberIdOrThrow(id);

        // 멤버 정보 업데이트
        existingMember.updateName(updatedMember.getName());
        existingMember.updateUsername(updatedMember.getUsername());
        existingMember.updateNickName(updatedMember.getNickname());
        existingMember.updateAge(updatedMember.getAge());

        // 업데이트된 멤버 저장
        memberRepository.save(existingMember);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findByMemberIdOrThrow(id);
        memberRepository.delete(member);
    }
}
