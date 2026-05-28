package com.sisc_it.sisc_rookie_web.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sisc_it.sisc_rookie_web.auth.dto.LoginRequest;
import com.sisc_it.sisc_rookie_web.auth.dto.LoginResponse;
import com.sisc_it.sisc_rookie_web.auth.dto.SignupRequest;
import com.sisc_it.sisc_rookie_web.auth.service.AuthService;
import com.sisc_it.sisc_rookie_web.global.exception.BusinessException;
import com.sisc_it.sisc_rookie_web.global.exception.ErrorCode;
import com.sisc_it.sisc_rookie_web.member.domain.Role;
import com.sisc_it.sisc_rookie_web.member.dto.MemberResponse;
import com.sisc_it.sisc_rookie_web.member.repository.MemberRepository;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void signupCreatesMemberWithEncodedPassword() {
        SignupRequest request = new SignupRequest(
            "Jin",
            "jin-signup@sisc.test",
            "password123",
            "https://example.com/jin.png"
        );

        MemberResponse response = authService.signup(request);

        assertThat(response.memberId()).isNotNull();
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.role()).isEqualTo(Role.MEMBER);
        assertThat(memberRepository.findByEmail(request.email()).orElseThrow().getPasswordHash())
            .isNotEqualTo(request.password());
    }

    @Test
    void signupRejectsDuplicateEmail() {
        SignupRequest request = new SignupRequest(
            "Duplicate",
            "duplicate@sisc.test",
            "password123",
            null
        );
        authService.signup(request);

        assertThatThrownBy(() -> authService.signup(request))
            .isInstanceOf(BusinessException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void loginReturnsAccessToken() {
        SignupRequest signupRequest = new SignupRequest(
            "Login User",
            "login-user@sisc.test",
            "password123",
            null
        );
        authService.signup(signupRequest);

        LoginResponse response = authService.login(new LoginRequest(signupRequest.email(), signupRequest.password()));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.email()).isEqualTo(signupRequest.email());
    }
}
