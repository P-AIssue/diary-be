package com.diary.myDiary.domain.diary.service.impl;

import com.diary.myDiary.domain.diary.dto.DiaryResponse;
import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.diary.service.DiaryService;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.global.auth.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final ChatGPTService chatGPTService;

    @Override
    public DiaryResponse create(String content, String emotion) {
        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        Diary diary = Diary.from(content, emotion, member);
        diaryRepository.save(diary);

        Map<String, Object> imageResult = chatGPTService.generateImageFromDiary(content);

        String imageUrl = extractImageUrl(imageResult);

        // Diary 엔티티에 이미지 URL 설정
        if (imageUrl != null) {
            diary.url(imageUrl);
            diaryRepository.save(diary);
        } else {
            log.error("이미지 생성에 실패하였습니다. 일기 ID: " + diary.getId());
        }

        return DiaryResponse.of(diary);
    }

    private String extractImageUrl(Map<String, Object> imageResult) {

        // 이미지 생성 API 응답에서 이미지 URL을 추출합니다.
        if (imageResult != null && imageResult.containsKey("data")) {
            Object dataObj = imageResult.get("data");
            if (dataObj instanceof List) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataObj;
                if (!dataList.isEmpty()) {
                    Map<String, Object> firstData = dataList.get(0);
                    if (firstData.containsKey("url")) {
                        return (String) firstData.get("url");
                    }
                }
            }
        }
        return null;
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
