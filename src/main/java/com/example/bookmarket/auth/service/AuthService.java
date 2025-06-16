package com.example.bookmarket.auth.service;

import com.example.bookmarket.auth.dto.AuthResponseDTO;
import com.example.bookmarket.auth.dto.LoginRequestDTO;
import com.example.bookmarket.auth.entity.RefreshToken;
import com.example.bookmarket.auth.exception.*;
import com.example.bookmarket.auth.repository.RefreshTokenRepository;
import com.example.bookmarket.auth.token.JwtTokenProvider;
import com.example.bookmarket.common.ErrorMessages;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.EMAIL_NOT_FOUND)); // 사용자가 존재하지 않으면 예외 발생

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { // 입력된 비밀번호와 저장된 비밀번호 비교
            throw new InvalidPasswordException(ErrorMessages.INVALID_PASSWORD);
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString()); // JWT 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail()); // 리프레시 토큰 생성

        refreshTokenRepository.save(RefreshToken.builder() // 리프레시 토큰 저장
//                .userId(user.getUsername()) // 사용자 ID 설정
                .userId(user.getEmail()) // ✅ username → email로 변경
                .token(refreshToken) // 생성된 리프레시 토큰 설정
                .expiryDate(Instant.now().plusMillis(jwtTokenProvider.refreshTokenValidityInMilliseconds)) // refresh token 유효기간 설정
                .build()); // 리프레시 토큰 엔티티 빌드 및 저장

        return AuthResponseDTO.builder() // 인증 응답 DTO 생성
                .accessToken(token) // 생성된 토큰 설정
                .refreshToken(refreshToken)
                .tokenType("Bearer") // 토큰 타입 설정
                .build(); // 빌더 패턴을 사용하여 AuthResponseDTO 객체 반환
    }



    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException(ErrorMessages.REFRESH_TOKEN_INVALID);
        }

        // JWT 토큰의 subject에서 email을 가져옴
        String email = jwtTokenProvider.getUsername(refreshToken);

        // 리프레시 토큰 저장소에서 해당 email로 리프레시 토큰 조회
        RefreshToken storedToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND));

        // 저장된 리프레시 토큰과 요청된 리프레시 토큰 비교
        if (!storedToken.getToken().equals(refreshToken)) {
            throw new RefreshTokenMismatchException(ErrorMessages.REFRESH_TOKEN_MISMATCH);
        }

        // 리프레시 토큰의 만료일이 현재 시간 이전인지 확인
        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException(ErrorMessages.REFRESH_TOKEN_EXPIRED);
        }

        // 사용자 엔티티 조회, 없으면 예외 던짐
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USERNAME_NOT_FOUND));

        // 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // 기존 refresh token 재사용 or 새로 발급 가능
                .tokenType("Bearer")
                .build();
    }
}
