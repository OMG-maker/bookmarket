package com.example.bookmarket.user.controller;

import com.example.bookmarket.user.dto.UserDTO;
import com.example.bookmarket.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // UserService를 주입받기 위한 필드
    private final UserService userService;

    // 생성자 주입을 통해 UserService를 주입받음
    public UserController(UserService userService) { this.userService = userService; }

    // 사용자 또는 관리자만 접근 가능한 엔드포인트
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/mypage")
    public ResponseEntity<String> userOrAdmin() {
        return ResponseEntity.ok("회원 또는 관리자 접근 가능 페이지입니다.");
    }

    // 사용자를 생성하는 엔드포인트 (관리자 전용)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        UserDTO savedUser = userService.save(dto); // 사용자를 저장
        return ResponseEntity.ok(savedUser); // HTTP 200 OK 응답으로 반환
    }

    // 모든 사용자를 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll(); // 모든 사용자를 조회
        return ResponseEntity.ok(users); // HTTP 200 OK 응답으로 반환
    }

    // ID로 사용자를 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id); // ID로 사용자를 조회, 없으면 예외 던짐
        return ResponseEntity.ok(user); // HTTP 200 OK 응답으로 반환
    }

    // ID로 사용자를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id); // ID로 사용자를 삭제, 없으면 예외 던짐
        return ResponseEntity.noContent().build(); // 204 반환
    }

    // ID로 사용자를 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.update(id, dto); // ID로 사용자를 수정, 없으면 예외 던짐
        return ResponseEntity.ok(updatedUser); // HTTP 200 OK 응답으로 반환
    }


}
