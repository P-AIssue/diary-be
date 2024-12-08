package com.diary.myDiary.domain.diary.repository;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.exception.DiaryException;
import com.diary.myDiary.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    default Diary getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new DiaryException(ErrorCode.NOT_FOUND_DIARY));
    }

    Page<Diary> findAll(Pageable pageable);

    // 해당 멤버의 일기들 가져오기
    List<Diary> findByMemberId(Long memberId);
}
