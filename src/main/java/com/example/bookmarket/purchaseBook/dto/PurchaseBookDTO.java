package com.example.bookmarket.purchaseBook.dto;

import com.example.bookmarket.purchaseBook.entity.PurchaseBook;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PurchaseBookDTO {
    private Long id;
    private Long purchaseId;
    private Long bookId;
    private Integer quantity;

    public static PurchaseBookDTO fromEntity(PurchaseBook purchaseBook) {
        return PurchaseBookDTO.builder()
                .id(purchaseBook.getId())
                .purchaseId(purchaseBook.getPurchase().getId())
                .bookId(purchaseBook.getBook().getId())
                .quantity(purchaseBook.getQuantity())
                .build();
    }
}
