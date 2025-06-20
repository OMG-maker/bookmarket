package com.example.bookmarket.requestedBook.exception;

public class RequestedBookNotFoundException extends RuntimeException {
    public RequestedBookNotFoundException(String message) {
        super(message);
    }
}

