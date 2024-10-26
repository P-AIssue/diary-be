package com.diary.myDiary.domain.diary.entity;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Table(name = "DIARY")
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // Diary 작성자

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 일기 내용

    @Column(name = "emotion_tag", length = 50)
    private String emotionTag; // 감정 태그

}
