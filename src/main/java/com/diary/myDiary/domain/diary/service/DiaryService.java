package com.diary.myDiary.domain.diary.service;

import com.diary.myDiary.domain.diary.dto.DiaryResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiaryService {

    DiaryResponse create(String content, String emotion);

    void remove(Long id);

    DiaryResponse getDiary(Long id);

    List<DiaryResponse> getDiaryList(HttpServletRequest request, Pageable pageable, int year);
}
