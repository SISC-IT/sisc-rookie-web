package com.sisc_it.sisc_rookie_web.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팀명 변경은 중복 확인이 필요하므로 DTO와 서비스 검증을 거친 뒤 갱신한다.
    @Column(nullable = false, unique = true)
    @Setter
    private String name;

    protected Team() {
    }

    public Team(String name) {
        this.name = name;
    }
}
