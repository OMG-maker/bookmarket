package com.example.bookmarket.purchase.controller;

import com.example.bookmarket.purchase.dto.PurchaseResponseDTO;
import com.example.bookmarket.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

//    @PostMapping
//    public ResponseEntity<Long> createPurchase() {
//        // 현재 인증된 사용자의 이메일을 가져옵니다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
//
//        // PurchaseService를 통해 구매를 생성합니다.
//        Long purchaseId = purchaseService.createPurchase(email);
//
//        // 생성된 구매 ID를 응답으로 반환합니다.
//        return ResponseEntity.ok(purchaseId);
//    }

    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername(); // 인증된 유저 이메일 획득

        Long purchaseId = purchaseService.createPurchase(userEmail);

        PurchaseResponseDTO response = PurchaseResponseDTO.builder()
                .purchaseId(purchaseId)
                .message("Purchase completed successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

}
