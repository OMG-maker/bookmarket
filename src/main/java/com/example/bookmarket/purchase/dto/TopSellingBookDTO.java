package com.example.bookmarket.purchase.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TopSellingBookDTO {
    private Long bookId;
    private Integer totalQuantity;
}
