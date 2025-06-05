package com.example.bookmarket.cart.dto;

import com.example.bookmarket.cart.entity.Cart;
import com.example.bookmarket.cartBook.dto.CartBookResponseDTO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private Cart.Status status;
    private List<CartBookResponseDTO> cartBooks;

    public static CartResponseDTO fromEntity(Cart cart) {
        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .status(cart.getStatus())
                .cartBooks(
                        cart.getCartBooks().stream()
                                .map(CartBookResponseDTO::fromEntity)
                                .toList()
                )
                .build();
    }
}

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true)
//public class CartResponseDTO {
//
//    private Long id;
//    private User user;
//    private Cart.Status status;
//    private List<CartBook> cartBooks = new ArrayList<>();
//
//
//    /**
//     * Cart 엔티티를 AddCartRequestDTO로 변환하는 정적 메소드
//     * @param cart 변환할 Cart 엔티티
//     * @return 변환된 AddCartRequestDTO 객체
//     */
//    public static CartResponseDTO fromEntity(Cart cart) {
//        return CartResponseDTO.builder()
//                .id(cart.getId())
//                .user(cart.getUser())
//                .status(cart.getStatus())
//                .cartBooks(cart.getCartBooks())
//                .build();
//    }
//
//    /**
//     * AddCartRequestDTO를 Cart 엔티티로 변환하는 메소드
//     * @return 변환된 Cart 엔티티
//     */
//    public Cart toEntity() {
//        return Cart.builder()
//                .id(this.id)
//                .user(this.user)
//                .status(this.status)
//                .cartBooks(this.cartBooks)
//                .build();
//
//    }
//
//}
