package com.sisc_it.sisc_rookie_web.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    // 이메일 변경은 중복 확인이 필요하므로 컨트롤러에서 바로 엔티티 setter를 호출하지 말고 DTO와 서비스 검증을 거친다.
    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    // 원문 비밀번호를 받는 DTO와 암호화 서비스가 생기면 그 경로에서만 갱신한다.
    @Column(nullable = false)
    @Setter
    private String passwordHash;

    @Setter
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Role role;

    // 팀 이동은 권한과 정원 같은 정책이 붙을 수 있으므로 DTO와 서비스 검증을 거친다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @Setter
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Position position;

    protected Member() {
    }

    public Member(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.position = Position.MEMBER;
    }

    public Member(String name, String email, String passwordHash, String profileImageUrl, Role role) {
        this(name, email, passwordHash, role);
        this.profileImageUrl = profileImageUrl;
    }

    public Member(String name, String email, String passwordHash, String profileImageUrl, Role role, Team team, Position position) {
        this(name, email, passwordHash, profileImageUrl, role);
        this.team = team;
        this.position = position;
    }
}
