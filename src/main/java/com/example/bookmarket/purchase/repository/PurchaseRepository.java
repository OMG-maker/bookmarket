package com.example.bookmarket.purchase.repository;

import com.example.bookmarket.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}