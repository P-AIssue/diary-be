package com.diary.myDiary.domain.SSE.repository;

import com.diary.myDiary.domain.SSE.entity.NotificationSSE;
import com.diary.myDiary.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationSSERepository extends JpaRepository<NotificationSSE, Long> {
    // 알림 전체 목록 조회
    List<NotificationSSE> findByMember(Member member);

}
