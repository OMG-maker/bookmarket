package com.example.bookmarket.cart.controller;

import com.example.bookmarket.cart.dto.CartResponseDTO;
import com.example.bookmarket.cart.service.CartService;
import com.example.bookmarket.cartBook.dto.AddCartBookRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId) {
        CartResponseDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<Void> addBookToCart(@RequestBody AddCartBookRequestDTO requestDTO) {
        cartService.addBookToCart(requestDTO.getUserId(), requestDTO.getBookId(), requestDTO.getQuantity());
        return ResponseEntity.ok().build();
    }

}
