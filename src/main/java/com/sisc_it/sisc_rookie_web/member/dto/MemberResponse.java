package com.sisc_it.sisc_rookie_web.member.dto;

import com.sisc_it.sisc_rookie_web.member.domain.Member;
import com.sisc_it.sisc_rookie_web.member.domain.Position;
import com.sisc_it.sisc_rookie_web.member.domain.Role;

public record MemberResponse(
    Long memberId,
    String name,
    String email,
    String profileImageUrl,
    Role role,
    Position position,
    Long teamId,
    String teamName
) {

    public static MemberResponse from(Member member) {
        Long teamId = member.getTeam() == null ? null : member.getTeam().getId();
        String teamName = member.getTeam() == null ? null : member.getTeam().getName();

        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getProfileImageUrl(),
            member.getRole(),
            member.getPosition(),
            teamId,
            teamName
        );
    }
}
