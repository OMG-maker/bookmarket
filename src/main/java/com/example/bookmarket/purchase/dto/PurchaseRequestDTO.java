package com.example.bookmarket.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PurchaseRequestDTO {
    @NotEmpty(message = "주문 도서 목록이 비어있습니다.")
    @Valid
    private List<BookOrderDTO> orders;

    @Getter
    @Setter
    public static class BookOrderDTO {
        @NotNull(message = "도서 ID는 필수입니다.")
        private Long bookId;

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        private Integer quantity;
    }
}
