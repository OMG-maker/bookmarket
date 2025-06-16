package com.example.bookmarket.book.repository;

import com.example.bookmarket.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Optional<Book> findByTitleAndAuthor(String title, String author); // 👈 title author 중복 체크용
}
