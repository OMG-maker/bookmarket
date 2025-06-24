package com.example.bookmarket.purchaseBook.repository;

import com.example.bookmarket.purchaseBook.entity.PurchaseBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseBookRepository extends JpaRepository<PurchaseBook, Long>, PurchaseBookRepositoryCustom {
}