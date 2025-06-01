package com.example.bookmarket.auth.service;

import com.example.bookmarket.auth.dto.AuthResponseDTO;
import com.example.bookmarket.auth.dto.LoginRequestDTO;
import com.example.bookmarket.auth.entity.RefreshToken;
import com.example.bookmarket.auth.repository.RefreshTokenRepository;
import com.example.bookmarket.auth.token.JwtTokenProvider;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 사용자 로그인 처리
     *
     * @param loginRequest 로그인 요청 DTO
     * @return 인증 응답 DTO
     */
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()) // 이메일로 사용자 조회
                .orElseThrow(() -> new RuntimeException("사용자 없음")); // 사용자가 존재하지 않으면 예외 발생

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { // 입력된 비밀번호와 저장된 비밀번호 비교
            throw new RuntimeException("비밀번호가 올바르지 않습니다."); // 비밀번호가 일치하지 않으면 예외 발생
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().toString()); // JWT 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getUsername())
                .token(refreshToken)
                .expiryDate(Instant.now().plusMillis(jwtTokenProvider.refreshTokenValidityInMilliseconds)) // refresh token 유효기간 설정
                .build());

        return AuthResponseDTO.builder() // 인증 응답 DTO 생성
                .accessToken(token) // 생성된 토큰 설정
                .refreshToken(refreshToken)
                .tokenType("Bearer") // 토큰 타입 설정
                .build(); // 빌더 패턴을 사용하여 AuthResponseDTO 객체 반환
    }



    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        RefreshToken storedToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Refresh Token이 존재하지 않습니다."));

        if (!storedToken.getToken().equals(refreshToken)) {
            throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
        }

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh Token이 만료되었습니다.");
        }

        // 사용자 엔티티 조회, 없으면 예외 던짐
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.createToken(username, user.getRole().toString());

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // 기존 refresh token 재사용 or 새로 발급 가능
                .tokenType("Bearer")
                .build();
    }
}
