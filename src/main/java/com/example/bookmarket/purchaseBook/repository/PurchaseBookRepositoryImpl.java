package com.example.bookmarket.purchaseBook.repository;

import com.example.bookmarket.purchase.dto.TopSellingBookDTO;
import com.example.bookmarket.purchase.entity.Purchase;
import com.example.bookmarket.purchase.entity.QPurchase;
import com.example.bookmarket.purchaseBook.entity.QPurchaseBook;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PurchaseBookRepositoryImpl implements PurchaseBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TopSellingBookDTO> findTopSellingBooksInPeriod(LocalDateTime start, LocalDateTime end, int limit) {
        QPurchaseBook pb = QPurchaseBook.purchaseBook;
        QPurchase p = QPurchase.purchase;

        return queryFactory
                .select(Projections.constructor(TopSellingBookDTO.class,
                        pb.book.id,
                        pb.quantity.sum()))
                .from(pb)
                .join(pb.purchase, p)
                .where(
                        p.purchasedAt.between(start, end),
                        p.status.eq(Purchase.Status.COMPLETED)
                )
                .groupBy(pb.book.id)
                .orderBy(pb.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }
}