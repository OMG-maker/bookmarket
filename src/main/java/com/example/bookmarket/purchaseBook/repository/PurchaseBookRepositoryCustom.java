package com.example.bookmarket.purchaseBook.repository;

import com.example.bookmarket.purchase.dto.TopSellingBookDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseBookRepositoryCustom {
    List<TopSellingBookDTO> findTopSellingBooksInPeriod(LocalDateTime start, LocalDateTime end, int limit);
}
