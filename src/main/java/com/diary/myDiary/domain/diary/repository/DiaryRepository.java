package com.diary.myDiary.domain.diary.repository;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.exception.DiaryException;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    default Diary getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new DiaryException(ErrorCode.NOT_FOUND_DIARY));
    }

    @Query("SELECT d FROM Diary d WHERE d.member = :member AND YEAR(d.createdDate) = :year")
    Page<Diary> findAllByMemberAndDate(
            @Param("member") Member member,
            @Param("year") int year,
            Pageable pageable
    );

    // 해당 멤버의 일기들 가져오기
    List<Diary> findByMemberId(Long memberId);

    // 받아온 년, 월 값의 해당하는 일기들 조회
    Page<Diary> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
