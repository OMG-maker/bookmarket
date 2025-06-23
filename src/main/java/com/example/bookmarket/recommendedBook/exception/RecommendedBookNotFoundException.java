package com.example.bookmarket.recommendedBook.exception;

public class RecommendedBookNotFoundException extends RuntimeException {
    public RecommendedBookNotFoundException(String message) {
        super(message);
    }
}
