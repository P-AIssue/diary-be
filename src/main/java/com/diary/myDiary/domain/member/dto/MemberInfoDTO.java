package com.diary.myDiary.domain.member.dto;

import com.diary.myDiary.domain.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDTO {

    private String name;

    private String nickname;

    private String username;

    private Integer age;

    @Builder
    public MemberInfoDTO(Member member) {
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
