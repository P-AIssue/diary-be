package com.diary.myDiary.domain.emotionAnalysis.entity;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionAnalysis extends BaseTimeEntity {

    // 감정분석 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long id;

    // diary_id  1:1 관계 매핑
    @OneToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary; // Diary 작성자

    // emotion_result
    @Column(name = "emotion_result", nullable = false)
    private String analysisResult; // emotion_result
}
