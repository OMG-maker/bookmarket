package com.example.bookmarket.book.repository;

import com.example.bookmarket.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthor(String title, String author); // 👈 title author 중복 체크용
//    List<Book> findByTitleAndAuthorContainingIgnoreCase(String title, String author); // 👈 title, author로 책 검색용 - 대소문자 무시
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
}
