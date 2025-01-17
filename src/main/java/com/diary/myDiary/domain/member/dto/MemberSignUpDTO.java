package com.diary.myDiary.domain.member.dto;

import com.diary.myDiary.domain.member.entity.Gender;
import com.diary.myDiary.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record MemberSignUpDTO(

        @NotBlank(message = "이메일을 입력해주세요")
        @Size(min = 7, max = 25, message = "이메일은 7자 이상, 25자 이하여야 합니다")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 형식이 아닙니다")
        String username,

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
                message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
        String password,

        @NotBlank(message = "이름을 입력해주세요") @Size(min = 2, message = "사용자 이름이 너무 짧습니다.")
        @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
        String name,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, message = "닉네임이 너무 짧습니다.")
        @NotBlank
        String nickname,

        @NotNull(message = "나이를 입력해주세요")
        @Range(min = 0, max = 150)
        Integer age,

        @NotNull(message = "성별을 선택해주세요")
        Gender gender

        ) {

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .build();
    }
}
