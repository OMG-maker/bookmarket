package com.example.bookmarket.book.repository;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> searchBooks(String title, String author, Pageable pageable) {
        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isBlank()) {
            builder.and(book.title.toLowerCase().contains(title.toLowerCase()));
        }

        if (author != null && !author.isBlank()) {
            builder.and(book.author.toLowerCase().contains(author.toLowerCase()));
        }

        List<Book> results = queryFactory
                .selectFrom(book)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(book.count())
                .from(book)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}