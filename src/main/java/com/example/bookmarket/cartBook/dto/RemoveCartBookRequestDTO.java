package com.example.bookmarket.cartBook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveCartBookRequestDTO {
    private Long userId;
    private Long bookId;
}
