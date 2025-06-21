package com.example.bookmarket.book.controller;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    // BookService를 주입받기 위한 필드
    private final BookService bookService;

    // 모든 책을 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.findAll(); // 모든 책을 조회
        return ResponseEntity.ok(books); // HTTP 200 OK 응답으로 반환
    }

    // ID로 책을 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.findById(id); // ID로 책을 조회, 없으면 예외 던짐
        return ResponseEntity.ok(book); // HTTP 200 OK 응답으로 반환
    }

    // 책 검색 페이지 추가
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> result = bookService.searchBooks(title, author, pageable);
        return ResponseEntity.ok(result);
    }
}