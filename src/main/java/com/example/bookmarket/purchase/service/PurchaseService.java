package com.example.bookmarket.purchase.service;


import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.cart.entity.Cart;
import com.example.bookmarket.cart.exception.CartEmptyException;
import com.example.bookmarket.cart.exception.CartNotFoundException;
import com.example.bookmarket.cart.repository.CartRepository;
import com.example.bookmarket.cartBook.entity.CartBook;
import com.example.bookmarket.cartBook.repository.CartBookRepository;
import com.example.bookmarket.purchase.entity.Purchase;
import com.example.bookmarket.purchase.exception.InsufficientStockException;
import com.example.bookmarket.purchase.exception.PaymentFailedException;
import com.example.bookmarket.purchase.exception.TotalPriceInvalidException;
import com.example.bookmarket.purchase.repository.PurchaseRepository;
import com.example.bookmarket.purchaseBook.entity.PurchaseBook;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartBookRepository cartBookRepository;

//    public Long createPurchase(PurchaseRequestDTO request, String userEmail) {
//        // TODO: 인증된 사용자 정보 받아오기 (예: SecurityContextHolder)
//        User currentUser = getCurrentAuthenticatedUser();
//
//        // 가정: 현재 사용자의 인증 정보를 가져오는 메소드
//        Purchase purchase = Purchase.builder()
//                .user(currentUser)
//                .purchasedAt(LocalDateTime.now())
////                .status(Purchase.Status.ACTIVE)
//                .status(Purchase.Status.COMPLETED) // 주문 상태를 COMPLETED로 설정 (데모 용도, 실제로는 주문 상태 관리 필요
//                                                        // - 배송 완료 등 이후 ACTIVE 에서 COMPLETED로 변경)
//                .build();
//
//        // 주문에 포함된 책들을 처리
//        List<PurchaseBook> purchaseBooks = new ArrayList<>();
//        // 총 가격 계산
//        BigDecimal totalPrice = BigDecimal.ZERO;
//
//        // 주문 요청에서 책 주문 목록을 순회하며 처리
//        for (PurchaseRequestDTO.BookOrderDTO order : request.getOrders()) {
//            // 책 정보 조회
//            Book book = bookRepository.findById(order.getBookId())
//                    .orElseThrow(() -> new RuntimeException("Book not found: " + order.getBookId()));
//            // 재고 확인
//            if (book.getStock() < order.getQuantity()) {
//                throw new RuntimeException("Insufficient stock for book: " + book.getTitle());
//            }
//
//            // 재고 감소
//            book.setStock(book.getStock() - order.getQuantity());
//
//            // 구매 책 정보 생성
//            PurchaseBook purchaseBook = PurchaseBook.builder()
//                    .purchase(purchase)
//                    .book(book)
//                    .quantity(order.getQuantity())
//                    .build();
//
//            // 구매 책 목록에 추가
//            purchaseBooks.add(purchaseBook);
//
//            // 총 가격 계산
//            totalPrice = totalPrice.add(book.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
//        }
//
//        // 구매 정보에 책 목록과 총 가격 설정
//        purchase.setPurchaseBooks(purchaseBooks);
//        purchase.setTotalPrice(totalPrice);
//
//        // 구매 정보 저장
//        purchaseRepository.save(purchase);
//
//        return purchase.getId();
//    }

    public Long createPurchase(String userEmail) {
        // 1. 인증된 유저 찾기
        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
                .orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND));

        // 2. 유저의 현재 Cart 찾기
        Cart cart = cartRepository.findByUserAndStatus(currentUser, Cart.Status.PENDING)
                .orElseThrow(() -> new CartNotFoundException(CART_NOT_FOUND));

        // 3. Purchase 엔티티 생성 (초기 상태 ACTIVE)
        Purchase purchase = Purchase.builder()
                .user(currentUser)
                .status(Purchase.Status.ACTIVE) // 결제 전 상태
                .build();

        // 4. CartBook 조회 및 PurchaseBook 생성
        List<CartBook> cartBooks = cartBookRepository.findByCart(cart);

        if (cartBooks.isEmpty()) {
            throw new CartEmptyException(CART_EMPTY);
        }

        List<PurchaseBook> purchaseBooks = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartBook cartBook : cartBooks) {
            Book book = cartBook.getBook();
            int quantity = cartBook.getQuantity();

            // 재고 확인 및 감소
            if (book.getStock() < quantity) {
                throw new InsufficientStockException(book.getTitle() + " : " + INSUFFICIENT_STOCK);
            }
            book.setStock(book.getStock() - quantity);

            // PurchaseBook 생성
            PurchaseBook purchaseBook = PurchaseBook.builder()
                    .purchase(purchase)
                    .book(book)
                    .quantity(quantity)
                    .build();

            purchaseBooks.add(purchaseBook);

            // 가격 누적
            totalPrice = totalPrice.add(book.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        // 5. Purchase에 책 목록과 총 가격 설정
        purchase.setPurchaseBooks(purchaseBooks);
        purchase.setTotalPrice(totalPrice);

        // 6. 결제 로직 (가상)
        boolean paymentSuccess = fakePaymentProcessing(totalPrice);

        if (!paymentSuccess) {
            throw new PaymentFailedException(PAYMENT_FAILED);
        }

        // 7. 결제 성공 시 상태 변경
        purchase.setStatus(Purchase.Status.COMPLETED);

        // 8. Cart 상태 ORDERED로 변경
        cart.setStatus(Cart.Status.ORDERED);

        // 9. 새로운 빈 Cart 생성해서 저장
        Cart newCart = Cart.builder()
                .user(currentUser)
                .status(Cart.Status.PENDING)
                .build();
        cartRepository.save(newCart);

        // 10. Purchase 저장 (cascade 때문에 purchaseBooks도 같이 저장됨)
        purchaseRepository.save(purchase);

        return purchase.getId();
    }

    private boolean fakePaymentProcessing(BigDecimal totalPrice) {
        // 실제 결제 시스템 연동 자리, 임시 성공 처리
        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TotalPriceInvalidException(TOTAL_PRICE_SMALL_THAN_ZERO);
        }

        return true;
    }

    /**
     * 현재 인증된 사용자의 정보를 가져오는 메소드
     * 실제 구현에서는 Spring Security 등을 통해 현재 로그인한 사용자의 정보를 가져와야 합니다.
     *
     * @return 현재 인증된 사용자
     */
    // 현재 로그인한 사용자의 정보를 가져오는 메소드 (Spring Security 연동 후 구현 예정)
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername(); // sub 값 = email

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)); // 현재 로그인한 사용자가 없으면 예외 발생
    }
}