package com.example.bookmarket.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     * 관리자 전용 대시보드 엔드포인트
     * @return 관리자 대시보드 접근 성공 메시지
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "관리자 대시보드 접근 성공!";
    }

    // 다른 관리자 전용 API들도 여기 추가
}
