package com.example.bookmarket.common;

import com.example.bookmarket.auth.dto.LoginRequestDTO;
import com.example.bookmarket.auth.dto.SignupRequestDto;
import com.example.bookmarket.auth.exception.*;
import com.example.bookmarket.book.exception.BookAlreadyExistsException;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.cart.exception.CartBookNotFoundException;
import com.example.bookmarket.cart.exception.CartEmptyException;
import com.example.bookmarket.cart.exception.CartNotFoundException;
import com.example.bookmarket.purchase.exception.InsufficientStockException;
import com.example.bookmarket.purchase.exception.InvalidPriceException;
import com.example.bookmarket.purchase.exception.PaymentFailedException;
import com.example.bookmarket.purchase.exception.TotalPriceInvalidException;
import com.example.bookmarket.user.exception.UserAlreadyExistsException;
import com.example.bookmarket.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이용자 파트
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    // 인증 파트
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPassword(InvalidPasswordException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<?> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<?> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenMismatchException.class)
    public ResponseEntity<?> handleRefreshTokenMismatch(RefreshTokenMismatchException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<?> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // 책 파트
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<?> handleBookAlreadyExists(BookAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(BookNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    // 장바구니 파트
    @ExceptionHandler(CartBookNotFoundException.class)
    public ResponseEntity<?> handleCartBookNotFound(CartBookNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<?> handleCartEmpty(CartEmptyException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound(CartNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }



    // 결제 파트
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleInsufficientStock(InsufficientStockException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<?> handleInvalidPrice(InvalidPriceException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<?> handlePaymentFailed(PaymentFailedException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(TotalPriceInvalidException.class)
    public ResponseEntity<?> handleTotalPriceInvalid(TotalPriceInvalidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<?> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    //

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//
//        // 첫 번째 에러 메시지 가져오기
//        String firstErrorMessage = ex.getBindingResult().getFieldErrors().stream()
//                .findFirst()
//                .map(FieldError::getDefaultMessage)
//                .orElse("Validation failed");
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
//        body.put("message", firstErrorMessage);  // 첫 에러 메시지로 대체
//        body.put("errors", errors);
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//
//        // 필드별 우선순위 지정
//        List<String> priorityOrder = List.of("email", "password", "username");
//
//        List<String> sortedMessages = new ArrayList<>();
//
//        // 우선순위 필드부터 메시지 추가
//        for (String field : priorityOrder) {
//            if (errors.containsKey(field)) {
//                sortedMessages.add(errors.get(field));
//                errors.remove(field);
//            }
//        }
//        // 우선순위에 없는 필드 메시지는 뒤에 추가
//        sortedMessages.addAll(errors.values());
//
//        // 첫 번째 메시지만 대표 메시지로 사용
//        String message = sortedMessages.isEmpty() ? "알 수 없는 오류가 발생했습니다." : sortedMessages.get(0);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
//        body.put("message", message);
//        // 모든 필드별 에러는 별도로 담아줌
//        body.put("errors", ex.getBindingResult().getFieldErrors().stream()
//                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Object target = ex.getBindingResult().getTarget();

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        List<String> priorityOrder;

        // DTO별 우선순위 지정
        if (target instanceof LoginRequestDTO) {
            priorityOrder = List.of("email", "password");
        } else if (target instanceof SignupRequestDto) {
            priorityOrder = List.of("email", "username", "password");
        } else {
            // 기타 DTO는 기본 우선순위
            priorityOrder = new ArrayList<>(errors.keySet());
        }

        List<String> sortedMessages = new ArrayList<>();
        for (String field : priorityOrder) {
            if (errors.containsKey(field)) {
                sortedMessages.add(errors.get(field));
                errors.remove(field);
            }
        }
        // 남은 메시지 추가
        sortedMessages.addAll(errors.values());

        // 첫 번째 메시지만 대표 메시지로 사용
        String message = sortedMessages.isEmpty() ? "유효성 검사 오류가 발생했습니다." : sortedMessages.get(0);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", message);
        body.put("errors", ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}