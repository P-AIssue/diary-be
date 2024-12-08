package com.diary.myDiary.domain.admin.service;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.notification.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    // 전체 멤버 리스트
    List<Member> getAllMembers();

    // 특정 멤버의 일기 리스트
    List<Diary> getMemberDiaries(Long memberId);

    // 특정 멤버의 알림 리스트
    List<Notification> getMemberNotifications(Long memberId);

    // 특정 일기의 상세 내용
    Diary getDiaryDetails(Long diaryId);
}


