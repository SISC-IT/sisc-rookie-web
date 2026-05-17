package com.sisc_it.sisc_rookie_web.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisc_it.sisc_rookie_web.team.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
