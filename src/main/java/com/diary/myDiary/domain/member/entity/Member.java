package com.diary.myDiary.domain.member.entity;

import com.diary.myDiary.domain.notification.entity.Notification;
import com.diary.myDiary.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //PK

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notification = new HashSet<>();

    @Column(nullable = false, length = 30, unique = true)
    private String username; //아이디

    private String password; //비밀번호

    @Column(nullable = false, length = 30)
    private String name; //이름(실명)

    @Column(nullable = false, length = 30)
    private String nickName; //별명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, length = 30)
    private Integer age; //나이

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role; //권한 -> USER, ADMIN

    @Column(length = 1000)
    private String refreshToken; //리프레시토큰

    //== 정보 수정 ==//
    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateAge(Integer age) {
        this.age = age;
    }

    //== 패스워드 암호화 ==//
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    //== 토큰 관련 ==//
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    //== 비밀번호 변경, 회원 탈퇴 시, 비밀번호를 확인하며, 이때 비밀번호의 일치여부를 판단하는 메서드 ==//
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches((checkPassword), getPassword());
    }

    //== 회원가입시, USER의 권한을 부여 ==//
    public void addUserAuthority() {
        this.role = Role.USER;
    }
}
