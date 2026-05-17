package com.sisc_it.sisc_rookie_web.team.domain;

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

    // Team name changes require duplicate checks, so route updates through DTO and service code.
    @Column(nullable = false, unique = true)
    @Setter
    private String name;

    protected Team() {
    }

    public Team(String name) {
        this.name = name;
    }
}
