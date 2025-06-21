package com.example.bookmarket.book.controller;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {
    // BookService를 주입받기 위한 필드
    private final BookService bookService;

    // 책을 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO dto) {
        BookDTO savedBook = bookService.save(dto); // 책을 저장
        return ResponseEntity.ok(savedBook); // HTTP 200 OK 응답으로 반환
    }

    // ID로 책을 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id); // ID로 책을 삭제, 없으면 예외 던짐
        return ResponseEntity.noContent().build(); // 204 반환
    }

    // ID로 책을 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO dto) {
        BookDTO updatedBook = bookService.update(id, dto); // ID로 사용자를 수정, 없으면 예외 던짐
        return ResponseEntity.ok(updatedBook); // HTTP 200 OK 응답으로 반환
    }
}
