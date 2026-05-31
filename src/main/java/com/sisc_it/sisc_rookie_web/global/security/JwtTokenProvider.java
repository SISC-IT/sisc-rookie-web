package com.sisc_it.sisc_rookie_web.global.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sisc_it.sisc_rookie_web.global.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sisc_it.sisc_rookie_web.global.exception.BusinessException;
import com.sisc_it.sisc_rookie_web.global.exception.ErrorCode;
import com.sisc_it.sisc_rookie_web.member.domain.Member;

@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;
    private final byte[] secret;
    private final long expirationSeconds;

    public JwtTokenProvider(
        ObjectMapper objectMapper,
        @Value("${security.jwt.secret:sisc-rookie-local-development-secret}") String secret,
        @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationSeconds;
    }

    public String createToken(Member member) {
        Instant now = Instant.now();
        Map<String, Object> header = Map.of(
            "alg", "HS256",
            "typ", "JWT"
        );
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", member.getEmail());
        payload.put("memberId", member.getId());
        payload.put("role", member.getRole().name());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", now.plusSeconds(expirationSeconds).getEpochSecond());

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String signature = sign(encodedHeader + "." + encodedPayload);

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public String getEmail(String token) {
        return String.valueOf(parseClaims(token).get("sub"));
    }

    public boolean validateToken(String token) {
        parseClaims(token);
        return true;
    }

    private Map<String, Object> parseClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
            }

            String unsignedToken = parts[0] + "." + parts[1];
            if (!sign(unsignedToken).equals(parts[2])) {
                throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
            }

            Map<String, Object> claims = objectMapper.readValue(
                BASE64_URL_DECODER.decode(parts[1]),
                new TypeReference<>() {
                }
            );
            Number expiration = (Number) claims.get("exp");
            if (expiration == null || expiration.longValue() < Instant.now().getEpochSecond()) {
                throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
            }

            return claims;
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (Exception exception) {
            throw new IllegalStateException("JWT JSON encoding failed.", exception);
        }
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("JWT signing failed.", exception);
        }
    }
}
