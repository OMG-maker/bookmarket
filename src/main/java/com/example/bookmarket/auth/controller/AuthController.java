package com.example.bookmarket.auth.controller;

import com.example.bookmarket.auth.dto.AuthResponseDTO;
import com.example.bookmarket.auth.dto.LoginRequestDTO;
import com.example.bookmarket.auth.dto.SignupRequestDto;
import com.example.bookmarket.auth.service.AuthService;
import com.example.bookmarket.user.dto.UserDTO;
import com.example.bookmarket.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * 로그인 엔드포인트
     * @param loginRequest 로그인 요청 DTO
     * @return 로그인 응답 DTO
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest); // 로그인 서비스 호출
        return ResponseEntity.ok(response); // HTTP 200 OK 응답으로 반환
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
     * 로그아웃 엔드포인트
     * @param request 로그아웃 요청 (토큰 포함)
     * @return 로그아웃 응답 DTO
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        AuthResponseDTO response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

}
