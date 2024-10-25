package com.diary.myDiary.domain.diary.entity;

import com.diary.myDiary.domain.emotionAnalysis.entity.EmotionAnalysis;
import com.diary.myDiary.domain.imageGeneration.entity.ImageGeneration;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Table(name = "DIARY")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Diary extends BaseTimeEntity {

    // 일기 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    // Diary 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    // 일기 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 감정 태그
    @Column(name = "emotion_tag", length = 50)
    private String emotionTag;

    // 이미지 생성과 1:1 매핑
    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL)
    private ImageGeneration imageGeneration;

    // 감정 분석과 1:1 매핑
    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL)
    private EmotionAnalysis emotionAnalysis;
}