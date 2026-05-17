package com.sisc_it.sisc_rookie_web.member.domain;

import com.sisc_it.sisc_rookie_web.team.domain.Team;
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

    // Email changes require duplicate checks. Route them through a DTO and service validation.
    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    // Accept raw passwords through DTOs only, then hash them in service code before updating this field.
    @Column(nullable = false)
    @Setter
    private String passwordHash;

    @Setter
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Role role;

    // Team changes can involve permissions and capacity rules, so validate them in service code.
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
