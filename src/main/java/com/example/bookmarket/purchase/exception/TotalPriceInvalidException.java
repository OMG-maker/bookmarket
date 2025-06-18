package com.example.bookmarket.purchase.exception;

public class TotalPriceInvalidException extends RuntimeException {
    public TotalPriceInvalidException(String message) {
        super(message);
    }
}