package com.diary.myDiary.domain.member.service.impl;

import com.diary.myDiary.domain.member.dto.MemberInfoDTO;
import com.diary.myDiary.domain.member.dto.MemberSignUpDTO;
import com.diary.myDiary.domain.member.dto.MemberUpdateDTO;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.exception.MemberException;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.domain.member.service.MemberService;
import com.diary.myDiary.global.auth.security.SecurityUtil;
import com.diary.myDiary.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(MemberSignUpDTO memberSignUpDTO) {

        Member member = memberSignUpDTO.toEntity();
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        if (memberRepository.existsByUsername(memberSignUpDTO.username())) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDTO memberUpdateDTO) {

        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        memberUpdateDTO.age().ifPresent(member::updateAge);
        memberUpdateDTO.name().ifPresent(member::updateName);
        memberUpdateDTO.nickName().ifPresent(member::updateNickName);
    }

    @Override
    public void updatePassword(String checkPassword, String newPassword) {

        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(ErrorCode.MISMATCH_PASSWORD);
        }

        member.updatePassword(passwordEncoder, newPassword);
    }

    @Override
    public void withdraw(String checkPassword) {

        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(ErrorCode.MISMATCH_PASSWORD);
        }

        memberRepository.delete(member);
    }

    @Override
    public MemberInfoDTO getInfo(Long id) throws Exception {

        Member findMember = memberRepository.findById(id).orElseThrow(() -> new Exception("회원이 존재하지 않습니다."));
        return new MemberInfoDTO(findMember);
    }

    @Override
    public MemberInfoDTO getMyInfo() {
        return new MemberInfoDTO(memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername()));
    }
}
