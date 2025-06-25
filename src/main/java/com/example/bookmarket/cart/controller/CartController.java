package com.example.bookmarket.cart.controller;

import com.example.bookmarket.cart.dto.CartResponseDTO;
import com.example.bookmarket.cart.service.CartService;
import com.example.bookmarket.cartBook.dto.AddCartBookRequestDTO;
import com.example.bookmarket.cartBook.dto.RemoveCartBookRequestDTO;
import com.example.bookmarket.cartBook.dto.UpdateCartBookRequestDTO;
import com.example.bookmarket.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId) {
        CartResponseDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/me")
    public ResponseEntity<CartResponseDTO> getMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        CartResponseDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }


    @PostMapping
    public ResponseEntity<Void> addBookToCart(@RequestBody AddCartBookRequestDTO requestDTO) {
        cartService.addBookToCart(requestDTO.getUserId(), requestDTO.getBookId(), requestDTO.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCartBook(@RequestBody UpdateCartBookRequestDTO requestDTO) {
        cartService.updateCartBookQuantity(requestDTO.getUserId(), requestDTO.getBookId(), requestDTO.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody RemoveCartBookRequestDTO requestDTO) {
        cartService.removeBookFromCart(requestDTO.getUserId(), requestDTO.getBookId());
        return ResponseEntity.ok().build();
    }




}
