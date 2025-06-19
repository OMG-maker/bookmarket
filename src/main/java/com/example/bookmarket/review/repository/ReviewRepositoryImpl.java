package com.example.bookmarket.review.repository;

import com.example.bookmarket.review.entity.QReview;
import com.example.bookmarket.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> searchReviews(Long userId, Long bookId, Pageable pageable) {
        QReview review = QReview.review;

        BooleanBuilder builder = new BooleanBuilder();

        if (userId != null) {
            builder.and(review.user.id.eq(userId)); // FK로 연결된 user의 id
        }

        if (bookId != null) {
            builder.and(review.book.id.eq(bookId)); // FK로 연결된 book의 id
        }

        List<Review> results = queryFactory
                .selectFrom(review)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(review.count())
                .from(review)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
