package com.example.bookmarket.purchase.service;


import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.purchase.dto.PurchaseRequestDTO;
import com.example.bookmarket.purchase.entity.Purchase;
import com.example.bookmarket.purchase.repository.PurchaseRepository;
import com.example.bookmarket.purchaseBook.entity.PurchaseBook;
import com.example.bookmarket.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final BookRepository bookRepository;

    public Long createPurchase(PurchaseRequestDTO request) {
        // TODO: 인증된 사용자 정보 받아오기 (예: SecurityContextHolder)
        User currentUser = getCurrentAuthenticatedUser();

        Purchase purchase = Purchase.builder()
                .user(currentUser)
                .purchasedAt(LocalDateTime.now())
                .status(Purchase.Status.ACTIVE)
                .build();

        List<PurchaseBook> purchaseBooks = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
//
//        for (PurchaseRequestDTO.BookOrderDTO order : request.getOrders()) {
//            Book book = bookRepository.findById(order.getBookId())
//                    .orElseThrow(() -> new RuntimeException("Book not found: " + order.getBookId()));
//
//            if (book.getStock() < order.getQuantity()) {
//                throw new RuntimeException("Insufficient stock for book: " + book.getTitle());
//            }
//
//            book.setStock(book.getStock() - order.getQuantity());
//
//            PurchaseBook purchaseBook = PurchaseBook.builder()
//                    .purchase(purchase)
//                    .book(book)
//                    .quantity(order.getQuantity())
//                    .build();
//
//            purchaseBooks.add(purchaseBook);
//
//            totalPrice = totalPrice.add(book.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
//        }

        purchase.setPurchaseBooks(purchaseBooks);
        purchase.setTotalPrice(totalPrice);

        purchaseRepository.save(purchase);

        return purchase.getId();
    }

    private User getCurrentAuthenticatedUser() {
        // TODO: Spring Security 연동 후 현재 로그인한 User 정보 가져오기
        return null;
    }
}