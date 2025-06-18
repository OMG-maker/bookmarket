package com.example.bookmarket.cart.exception;

public class CartBookNotFoundException extends RuntimeException {
    public CartBookNotFoundException(String message) {
        super(message);
    }
}