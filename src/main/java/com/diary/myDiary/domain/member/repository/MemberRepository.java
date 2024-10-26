package com.diary.myDiary.domain.member.repository;

import com.diary.myDiary.domain.member.entity.Member;
import com.diary.myDiary.domain.member.exception.MemberException;
import com.diary.myDiary.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByUsernameOrThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);
}
