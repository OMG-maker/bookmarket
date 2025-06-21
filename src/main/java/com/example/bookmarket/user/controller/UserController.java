package com.example.bookmarket.user.controller;

import com.example.bookmarket.user.dto.UserDTO;
import com.example.bookmarket.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.bookmarket.common.ResponseMessage.PAGE_FOR_USER_AND_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    // UserService를 주입받기 위한 필드
    private final UserService userService;

    // 사용자 또는 관리자만 접근 가능한 엔드포인트
    @GetMapping("/mypage")
    public ResponseEntity<String> userOrAdmin() {
        return ResponseEntity.ok(PAGE_FOR_USER_AND_ADMIN);
    }

    // 현재 로그인한 사용자의 정보를 조회하는 엔드포인트
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();
        UserDTO user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

}
