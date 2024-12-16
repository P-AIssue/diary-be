package com.diary.myDiary.domain.diary.service.impl;

import com.diary.myDiary.domain.diary.dto.DiaryResponse;
import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.diary.service.DiaryService;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.exception.MemberException;
import com.diary.myDiary.domain.member.repository.MemberRepository;
import com.diary.myDiary.global.auth.security.SecurityUtil;
import com.diary.myDiary.global.auth.service.JwtService;
import com.diary.myDiary.global.exception.ErrorCode;
import com.diary.myDiary.global.util.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final ChatGPTService chatGPTService;
    private final JwtService jwtService;

    @Override
    public DiaryResponse create(String content, String emotion) {
        Member member = memberRepository.getByUsernameOrThrow(SecurityUtil.getLoginUsername());

        Map<String, Object> imageResult = chatGPTService.generateImageFromDiary(content);

        String imageUrl = extractImageUrl(imageResult);


        String encryptedContent;
        try {
            encryptedContent = AESUtil.encrypt(content);
        } catch (Exception e) {
            log.error("일기내용 암호화 실패", e);
            throw new RuntimeException("암호화 중 오류 발생");
        }
        Diary diary = Diary.from(encryptedContent, emotion, member);
        // Diary 엔티티에 이미지 URL 설정
        if (imageUrl != null) {
            diary.setUrl(imageUrl);
        } else {
            log.error("이미지 생성에 실패하였습니다. 일기 ID: " + diary.getId());
        }

        diaryRepository.save(diary);

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

        // 일기 내용을 복호화
        String decryptedContent;
        try {
            decryptedContent = AESUtil.decrypt(diary.getContent());
        } catch (Exception e) {
            log.error("일기 내용 복호화 실패", e);
            throw new RuntimeException("복호화 중 오류 발생");
        }

        // 복호화된 내용을 DiaryResponse에 전달
        return new DiaryResponse(
                diary.getId(),
                decryptedContent,
                diary.getEmotionTag(),
                diary.getImageUrl(),
                diary.getCreatedDate()
        );

    }

    @Override
    public List<DiaryResponse> getDiaryList(String token, Pageable pageable) {
        String username = jwtService.extractUsername(token)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_ACCESS_TOKEN));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        Page<Diary> diaryPage = diaryRepository.findAllByMember(member, pageable);
        List<Diary> diaries = diaryPage.getContent();

        return DiaryResponse.listOf(diaries);
    }
}