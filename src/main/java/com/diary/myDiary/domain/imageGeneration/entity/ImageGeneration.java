package com.diary.myDiary.domain.imageGeneration.entity;


import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "ImageGeneration")
public class ImageGeneration extends BaseTimeEntity {

    // Image ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageGeneration_id", nullable = false)
    private Long id;

    // diary 1:1 관계 매핑
    @OneToOne
    @JoinColumn(name = "diary_id", nullable = false)
    @ToString.Exclude
    private Diary diary;

    // image url
    @Column(nullable = false, length = 1000)
    private String url;
}
