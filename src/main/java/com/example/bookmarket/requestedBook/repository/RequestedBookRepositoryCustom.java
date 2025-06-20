package com.example.bookmarket.requestedBook.repository;

import com.example.bookmarket.requestedBook.entity.RequestedBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface RequestedBookRepositoryCustom {
    Page<RequestedBook> searchRequestedBooks(Long userId, String title, String author, String isbn, String publisher, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
