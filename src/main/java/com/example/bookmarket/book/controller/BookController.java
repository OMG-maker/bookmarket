package com.example.bookmarket.book.controller;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    // BookService를 주입받기 위한 필드
    private final BookService bookService;

    // 생성자 주입을 통해 BookService를 주입받음
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 책을 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO dto) {
        BookDTO savedBook = bookService.save(dto); // 책을 저장
        return ResponseEntity.ok(savedBook); // HTTP 200 OK 응답으로 반환
    }

    // 모든 책을 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.findAll(); // 모든 책을 조회
        return ResponseEntity.ok(books); // HTTP 200 OK 응답으로 반환
    }

    // ID로 책을 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.findById(id); // ID로 책을 조회
        if (book != null) {
            return ResponseEntity.ok(book); // HTTP 200 OK 응답으로 반환
        } else {
            return ResponseEntity.notFound().build(); // 책이 존재하지 않으면 HTTP 404 Not Found 응답 반환
        }
    }

    // ID로 책을 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookService.existsById(id)) { // ID로 책이 존재하는지 확인
            return ResponseEntity.notFound().build(); // 404 반환
        }
        bookService.deleteById(id); // ID로 책을 삭제
        return ResponseEntity.noContent().build(); // 204 반환
    }

    // ID로 책을 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO dto) {
        BookDTO updatedBook = bookService.update(id, dto); // ID로 책을 수정
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook); // HTTP 200 OK 응답으로 반환
        } else {
            return ResponseEntity.notFound().build(); // 책이 존재하지 않으면 HTTP 404 Not Found 응답 반환
        }
    }
}