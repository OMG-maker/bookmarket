package com.example.bookmarket.cartBook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCartBookRequestDTO {
    private Long userId;
    private Long bookId;
    private int quantity;
}
