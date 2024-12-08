package com.diary.myDiary.domain.admin.service.impl;

import com.diary.myDiary.domain.admin.service.AdminService;
import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final NotificationRepository notificationRepository;

    public AdminServiceImpl(MemberRepository memberRepository, DiaryRepository diaryRepository, NotificationRepository notificationRepository) {
        this.memberRepository = memberRepository;
        this.diaryRepository = diaryRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    // 전체 멤버 리스트
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    // 특정 멤버의 일기 리스트
    public List<Diary> getMemberDiaries(Long memberId) {
        return diaryRepository.findByMemberId(memberId);
    }

    @Override
    // 특정 멤버의 알림 리스트
    public List<Notification> getMemberNotifications(Long memberId) {
        return notificationRepository.findByMemberIdOrThrow(memberId);
    }

    @Override
    // 특정 일기의 상세 내용
    public Diary getDiaryDetails(Long diaryId) {
        return diaryRepository.getByIdOrThrow(diaryId);
    }
}

