package com.example.bookmarket.cart.repository;

import com.example.bookmarket.cart.entity.Cart;
import com.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, Cart.Status status);
}