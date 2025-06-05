package com.example.bookmarket.cartBook.dto;

import com.example.bookmarket.cartBook.entity.CartBook;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartBookResponseDTO {
    private Long bookId;
    private String title;
    private int quantity;

    public static CartBookResponseDTO fromEntity(CartBook cb) {
        return CartBookResponseDTO.builder()
                .bookId(cb.getBook().getId())
                .title(cb.getBook().getTitle())
                .quantity(cb.getQuantity())
                .build();
    }
}