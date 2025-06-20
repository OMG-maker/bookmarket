package com.example.bookmarket.requestedBook.repository;

import com.example.bookmarket.requestedBook.entity.QRequestedBook;
import com.example.bookmarket.requestedBook.entity.RequestedBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class RequestedBookRepositoryImpl implements RequestedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestedBook> searchRequestedBooks(Long userId, String title, String author, String isbn, String publisher, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        QRequestedBook requestedBook = QRequestedBook.requestedBook;

        BooleanBuilder builder = new BooleanBuilder();

        // FK로 연결된 user의 id
        if (userId != null) { builder.and(requestedBook.user.id.eq(userId)); }
        // 찾는 희망 도서의 제목
        if (title != null && !title.isEmpty()) { builder.and(requestedBook.title.containsIgnoreCase(title)); }
        // 찾는 희망 도서의 저자
        if (author != null && !author.isEmpty()) { builder.and(requestedBook.author.containsIgnoreCase(author)); }
        // 찾는 희망 도서의 고유번호
        if (isbn != null && !isbn.isEmpty()) { builder.and(requestedBook.isbn.eq(isbn)); }
        // 찾는 희망 도서의 출판사
        if (publisher != null && !publisher.isEmpty()) { builder.and(requestedBook.publisher.containsIgnoreCase(publisher)); }
        // 희망 도서의 구매 날짜 범위
        if (startDate != null && endDate != null) { // startDate와 endDate가 모두 null이 아닐 때
            builder.and(requestedBook.requestedAt.between(startDate, endDate)); // 두 날짜 사이
        } else if (startDate != null) { // startDate가 null이 아닐 때
            builder.and(requestedBook.requestedAt.goe(startDate)); // 시작 날짜 이후
        } else if (endDate != null) { // endDate가 null이 아닐 때
            builder.and(requestedBook.requestedAt.loe(endDate)); // 종료 날짜 이전
        }

        List<RequestedBook> results = queryFactory
                .selectFrom(requestedBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(requestedBook.count())
                .from(requestedBook)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
