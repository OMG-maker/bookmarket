package com.example.bookmarket.auth.token;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "your-secret-key"; // 나중에 application.properties에서 관리
    private final long validityInMilliseconds = 3600000; // 1시간
//    private final long validityInMilliseconds = 15000; // 5초

    // 만료 시간 예시: 7일
    public final long refreshTokenValidityInMilliseconds = 7 * 24 * 60 * 60 * 1000;

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param email 사용자 이메일
     * @param role 사용자 역할 (예: USER, ADMIN)
     * @return 생성된 JWT 토큰
     */
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email); // 사용자 이름을 클레임에 설정
        claims.put("role", role); // 사용자 역할을 클레임에 추가
        Date now = new Date(); // 현재 시간
        Date validity = new Date(now.getTime() + validityInMilliseconds); // 토큰 유효 기간 설정

        return Jwts.builder() // JWT 빌더 사용
                .setClaims(claims) // 클레임 설정
                .setIssuedAt(now) // 발행 시간 설정
                .setExpiration(validity) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 비밀 키로 서명
                .compact(); // 토큰 생성
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     *
     * @param token JWT 토큰
     * @return 유효한 경우 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // 토큰 파싱 및 서명 검증
            return !claims.getBody().getExpiration().before(new Date()); // 만료 시간 검증
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 리프레시 토큰을 생성합니다.
     *
     * @param email 사용자 이름
     * @return 생성된 리프레시 토큰
     */
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // 토큰에서 사용자 이름 추출
    }

    /**
     * JWT 토큰에서 비밀 키를 추출합니다.
     * @return 비밀 키
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * JWT 토큰의 만료 시간을 반환합니다.
     *
     * @param token JWT 토큰
     * @return 만료 시간 (밀리초 단위)
     */
    public long getExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰, 없으면 null
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 다음의 실제 토큰만 추출
        }
        return null;
    }
}