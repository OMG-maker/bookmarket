package com.example.bookmarket.recommendedBook.repository;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long>, RecommendedBookRepositoryCustom {
    boolean existsByBookAndTypeAndCreatedAtBetween(Book book, RecommendedBook.Type type,
                                                   LocalDateTime start, LocalDateTime end);
}
