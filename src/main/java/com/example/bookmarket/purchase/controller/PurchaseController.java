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

import static com.example.bookmarket.common.ResponseMessage.PURCHASE_COMPLETED;

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

    /**
     * 구매를 생성하는 API 엔드포인트입니다.
     * 현재 인증된 사용자의 이메일을 사용하여 구매를 생성합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @return 생성된 구매 ID와 메시지를 포함한 응답 객체
     */
    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@AuthenticationPrincipal UserDetails userDetails) {
        // 현재 인증된 사용자의 이메일을 가져옵니다.
        String userEmail = userDetails.getUsername();
        // PurchaseService를 통해 구매를 생성합니다.
        Long purchaseId = purchaseService.createPurchase(userEmail);
        // 생성된 구매 ID와 메시지를 포함한 응답 객체를 생성합니다.
        PurchaseResponseDTO response = PurchaseResponseDTO.builder()
                .purchaseId(purchaseId)
                .message(PURCHASE_COMPLETED)
                .build();
        // 응답 객체를 ResponseEntity로 감싸서 반환합니다.
        return ResponseEntity.ok(response);
    }

}
