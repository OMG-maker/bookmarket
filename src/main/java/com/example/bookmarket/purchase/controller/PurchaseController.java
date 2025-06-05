package com.example.bookmarket.purchase.controller;

import com.example.bookmarket.purchase.dto.PurchaseRequestDTO;
import com.example.bookmarket.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Long> createPurchase(@RequestBody @Valid PurchaseRequestDTO request) {
        Long purchaseId = purchaseService.createPurchase(request);
        return ResponseEntity.ok(purchaseId);
    }
}
