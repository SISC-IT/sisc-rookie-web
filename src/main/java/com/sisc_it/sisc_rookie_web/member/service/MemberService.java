package com.sisc_it.sisc_rookie_web.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisc_it.sisc_rookie_web.global.exception.BusinessException;
import com.sisc_it.sisc_rookie_web.global.exception.ErrorCode;
import com.sisc_it.sisc_rookie_web.member.domain.Member;
import com.sisc_it.sisc_rookie_web.member.dto.MemberResponse;
import com.sisc_it.sisc_rookie_web.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMyProfile(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}
