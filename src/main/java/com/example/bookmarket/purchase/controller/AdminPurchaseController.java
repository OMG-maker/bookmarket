package com.example.bookmarket.purchase.controller;

import com.example.bookmarket.purchase.dto.PurchaseDTO;
import com.example.bookmarket.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/admin/purchases")
public class AdminPurchaseController {

    // UserService를 주입받기 위한 필드
    private final PurchaseService purchaseService;

    // 모든 사용자를 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases() {
        List<PurchaseDTO> purchases = purchaseService.findAll(); // 모든 사용자를 조회
        return ResponseEntity.ok(purchases); // HTTP 200 OK 응답으로 반환
    }

}
