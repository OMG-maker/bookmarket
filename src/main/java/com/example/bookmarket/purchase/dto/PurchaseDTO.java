package com.example.bookmarket.purchase.dto;

import com.example.bookmarket.purchase.entity.Purchase;
import com.example.bookmarket.purchaseBook.dto.PurchaseBookDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PurchaseDTO {
    private Long purchaseId;
    private LocalDateTime purchasedAt;
    private Long userId;
    private BigDecimal totalPrice;
    private Purchase.Status status;
    private List<PurchaseBookDTO> purchaseBooks;

    public static PurchaseDTO fromEntity(Purchase purchase) {
        return PurchaseDTO.builder()
                .purchaseId(purchase.getId())
                .purchasedAt(purchase.getPurchasedAt())
                .userId(purchase.getUser().getId())
                .totalPrice(purchase.getTotalPrice())
                .status(purchase.getStatus())
//                .purchaseBooks(purchase.getPurchaseBooks())
                .purchaseBooks(
                        purchase.getPurchaseBooks().stream()
                                .map(PurchaseBookDTO::fromEntity)
                                .toList()
                )
                .build();
    }
}
