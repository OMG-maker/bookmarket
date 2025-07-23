package com.example.bookmarket.auth.controller;

import com.example.bookmarket.auth.dto.AuthResponseDTO;
import com.example.bookmarket.auth.dto.LoginRequestDTO;
import com.example.bookmarket.auth.dto.SignupRequestDto;
import com.example.bookmarket.auth.service.AuthService;
import com.example.bookmarket.auth.token.JwtTokenProvider;
import com.example.bookmarket.user.dto.UserDTO;
import com.example.bookmarket.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.bookmarket.common.ResponseMessage.LOGOUT_SUCCESS;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthController(
            AuthService authService,
            UserService userService,
            JwtTokenProvider jwtTokenProvider,
            RedisTemplate<String, String> redisTemplate) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 로그인 엔드포인트
     * @param loginRequest 로그인 요청 DTO
     * @return 로그인 응답 DTO
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest); // 로그인 서비스 호출
        return ResponseEntity.ok(response); // HTTP 200 OK 응답으로 반환
    }

    /**
     * 로그아웃 엔드포인트
     * @param request HTTP 요청 객체
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            long expiration = jwtTokenProvider.getExpiration(token);
            redisTemplate.opsForValue().set("logout:" + token, "true", expiration, TimeUnit.MILLISECONDS);
        }

        return ResponseEntity.ok(LOGOUT_SUCCESS);
    }

    /**
     * 회원가입 엔드포인트
     * @param dto 회원가입 요청 DTO
     * @return 생성된 사용자 DTO
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid SignupRequestDto dto) {
        UserDTO savedUser = userService.save(dto); // 사용자를 저장
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // HTTP 201 Created 응답으로 반환
    }



    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받는 엔드포인트
     * @param request 리프레시 토큰을 포함한 요청
     * @return 새로운 액세스 토큰을 포함한 응답 DTO
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        AuthResponseDTO response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

}
