package com.example.bookmarket.purchase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponseDTO {
    private Long purchaseId;
    private String message;
}