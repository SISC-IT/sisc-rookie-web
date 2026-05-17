package com.sisc_it.sisc_rookie_web.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisc_it.sisc_rookie_web.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
