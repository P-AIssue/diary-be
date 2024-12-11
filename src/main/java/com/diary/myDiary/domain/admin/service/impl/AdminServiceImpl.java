package com.diary.myDiary.domain.admin.service.impl;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.domain.admin.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    public AdminServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 멤버 리스트
    @Override
    public List<MemberInfoDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberInfoDTO::new)
                .collect(Collectors.toList());
    }

    // 멤버 아이디 가져오기
    @Override
    public MemberInfoDTO getMemberById(Long id) {
        Member member = memberRepository.findByMemberIdOrThrow(id);
        return new MemberInfoDTO(member);
    }

    // 멤버 수정
    @Override
    public void updateMember(Long id, MemberInfoDTO memberDto) {
        Member member = memberRepository.findByMemberIdOrThrow(id);

        // 업데이트 로직
        member.updateNickName(memberDto.getNickname());
        memberRepository.save(member);
    }

    // 멤버 삭제
    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findByMemberIdOrThrow(id);
        memberRepository.delete(member);
    }
}
