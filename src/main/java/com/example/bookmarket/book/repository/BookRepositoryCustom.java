package com.example.bookmarket.book.repository;

import com.example.bookmarket.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<Book> searchBooks(String title, String author, Pageable pageable);
}