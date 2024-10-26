package com.diary.myDiary.domain.diary.service.impl;

import com.diary.myDiary.domain.diary.dto.DiaryResponse;
import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.diary.service.DiaryService;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.global.auth.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Override
    public DiaryResponse create(String content, String emotion) {
        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        Diary diary = Diary.from(content, emotion, member);
        diaryRepository.save(diary);

        return DiaryResponse.of(diary);
    }

    @Override
    public void remove(Long id) {
        Diary diary = diaryRepository.getByIdOrThrow(id);
        diaryRepository.delete(diary);
    }

    @Override
    public DiaryResponse getDiary(Long id) {
        Diary diary = diaryRepository.getByIdOrThrow(id);

        return DiaryResponse.of(diary);
    }

    @Override
    public List<DiaryResponse> getDiaryList(Pageable pageable) {
        Page<Diary> diaryPage = diaryRepository.findAll(pageable);
        List<Diary> diaries = diaryPage.getContent();

        return DiaryResponse.listOf(diaries);
    }
}
