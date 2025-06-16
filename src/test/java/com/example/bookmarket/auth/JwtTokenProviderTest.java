package com.example.bookmarket.auth;

import com.example.bookmarket.auth.token.JwtTokenProvider;
import com.example.bookmarket.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰을 생성하고, 유효성을 검증하는 테스트
     * - 사용자 이름과 역할을 기반으로 토큰을 생성
     * - 생성된 토큰이 유효한지 검증
     * - 토큰에서 사용자 이름을 추출하여 원본과 일치하는지 확인
     */
    @Test
    public void testGenerateAndValidateToken() {
//        String username = "testuser";
//        String role = User.Role.USER.name();
//
//        String token = jwtTokenProvider.createToken(username, role);
//
//        assertTrue(jwtTokenProvider.validateToken(token));
//
//        String extractedUsername = jwtTokenProvider.getUsername(token);
//        assertEquals(username, extractedUsername);

        String email = "testuser@example.com"; // 이메일로 변경
        String role = User.Role.USER.name();

        String token = jwtTokenProvider.createToken(email, role);

        assertTrue(jwtTokenProvider.validateToken(token));

        String extractedEmail = jwtTokenProvider.getUsername(token);
        assertEquals(email, extractedEmail);
    }

    /**
     * 유효하지 않은 JWT 토큰을 검증하는 테스트
     * - 잘못된 서명 또는 형식의 토큰을 사용하여 유효성 검증 실패 확인
     */
    @Test
    public void testExpiredToken() {
        // 1초 전으로 만료된 토큰 생성
        String expiredToken = Jwts.builder() // JWT 빌더 사용
                .setSubject("expiredUser") // 만료된 사용자 이름
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 이미 만료된 시점
                .signWith(SignatureAlgorithm.HS256, jwtTokenProvider.getSecretKey()) // secret key 가져오기
                .compact(); // 토큰 생성

        assertFalse(jwtTokenProvider.validateToken(expiredToken)); // 유효성 검증 실패 확인
    }
}
