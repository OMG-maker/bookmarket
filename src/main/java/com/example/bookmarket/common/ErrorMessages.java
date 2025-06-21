package com.example.bookmarket.common;


public class ErrorMessages {
    // 회원가입, 사용자 관련
    public static final String USER_NOT_FOUND = "해당 ID의 사용자가 존재하지 않습니다.";
    public static final String USER_ALREADY_EXISTS = "이미 존재하는 사용자입니다.";
    public static final String USER_EMAIL_ALREADY_EXISTS = "이미 존재하는 이메일입니다.";

    // 로그인 관련
    public static final String INVALID_PASSWORD = "비밀번호가 올바르지 않습니다.";
    public static final String EMAIL_NOT_FOUND = "해당 이메일을 가진 사용자가 존재하지 않습니다.";
    public static final String USERNAME_NOT_FOUND = "해당 이름을 가진 사용자가 존재하지 않습니다.";
    public static final String REFRESH_TOKEN_INVALID = "Refresh Token이 유효하지 않습니다.";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh Token이 만료되었습니다.";
    public static final String REFRESH_TOKEN_MISMATCH = "Refresh Token이 일치하지 않습니다.";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh Token이 존재하지 않습니다.";

    // 결제 관련 관련
    public static final String CART_NOT_FOUND = "장바구니를 찾을 수 없습니다.";
    public static final String CART_EMPTY = "장바구니가 비어있습니다.";
    public static final String INSUFFICIENT_STOCK = "재고가 부족합니다.";
    public static final String PAYMENT_FAILED = "결제에 실패했습니다.";
    public static final String INVALID_PRICE = "유효하지 않은 가격입니다.";
    public static final String TOTAL_PRICE_SMALL_THAN_ZERO = "총 가격은 0보다 커야 합니다.";
    public static final String CART_BOOK_NOT_FOUND = "장바구니에 해당 도서가 없습니다.";

    // 리뷰 관련
    public static final String REVIEW_NOT_FOUND = "해당 ID의 리뷰가 존재하지 않습니다.";
    public static final String REVIEW_NOT_OWNED = "본인의 리뷰가 아닙니다.";

    // 희망 도서 관련
    public static final String REQUESTED_BOOK_NOT_FOUND = "해당 ID의 희망 도서가 존재하지 않습니다.";
    public static final String REQUESTED_BOOK_NOT_OWNED = "본인의 희망도서가 아닙니다.";

    // 책 관련
    public static final String BOOK_NOT_FOUND = "해당 ID의 책이 존재하지 않습니다.";
    public static final String BOOK_ALREADY_EXISTS = "이미 존재하는 책입니다.";

    // 필요한 에러 메시지 계속 추가 가능
}