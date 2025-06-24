package com.example.bookmarket.recommendedBook.repository;

import com.example.bookmarket.recommendedBook.dto.RecommendedBookSearchCondition;
import com.example.bookmarket.recommendedBook.entity.QRecommendedBook;
import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RecommendedBookRepositoryImpl implements RecommendedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deactivateRecommendedBooksByType(RecommendedBook.Type type) {
        QRecommendedBook rb = QRecommendedBook.recommendedBook;

        queryFactory.update(rb)
                .set(rb.status, RecommendedBook.Status.INACTIVE)
                .where(rb.type.eq(type))
                .execute();
    }

    @Override
    public List<RecommendedBook> findBySearchCondition(RecommendedBookSearchCondition condition) {
        QRecommendedBook rb = QRecommendedBook.recommendedBook;
        BooleanBuilder builder = new BooleanBuilder();

//        builder.and(rb.status.eq(RecommendedBook.Status.ACTIVE)); // 활성화된 추천 도서만 조회

        if (condition.getType() != null) { builder.and(rb.type.eq(condition.getType())); }
        if (condition.getFromDate() != null) { builder.and(rb.createdAt.goe(condition.getFromDate().atStartOfDay())); } // 시작 날짜 이후
        if (condition.getToDate() != null) { builder.and(rb.createdAt.lt(condition.getToDate().plusDays(1).atStartOfDay())); } // 시작 날짜 이후

        return queryFactory
                .selectFrom(rb)
                .where(builder)
//                .orderBy(rb.createdAt.desc())
                .fetch();
    }
}
