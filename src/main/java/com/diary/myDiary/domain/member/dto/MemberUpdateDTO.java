package com.diary.myDiary.domain.member.dto;

import java.util.Optional;

public record MemberUpdateDTO(
        Optional<String> nickName
) {
}
