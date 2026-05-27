package com.sisc_it.sisc_rookie_web.auth.dto;

import com.sisc_it.sisc_rookie_web.member.domain.Role;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long memberId,
    String name,
    String email,
    Role role
) {
}
