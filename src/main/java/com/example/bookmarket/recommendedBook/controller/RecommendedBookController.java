package com.example.bookmarket.recommendedBook.controller;

import com.example.bookmarket.recommendedBook.dto.RecommendedBookDTO;
import com.example.bookmarket.recommendedBook.service.RecommendedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommended-books")
public class RecommendedBookController {

    private final RecommendedBookService recommendedBookService;

    // 추천 도서 조회 엔드포인트
     @GetMapping
     public ResponseEntity<List<RecommendedBookDTO>> getAllRecommendedBooks() {
            List<RecommendedBookDTO> recommendedBooks = recommendedBookService.findAll();
            return ResponseEntity.ok(recommendedBooks);
     }

    // ID로 추천 도서를 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<RecommendedBookDTO> getRecommendedBookById(@PathVariable Long id) {
        RecommendedBookDTO recommendedBook = recommendedBookService.findById(id); // ID로 희망 도서를 조회, 없으면 예외 던짐
        return ResponseEntity.ok(recommendedBook); // HTTP 200 OK 응답으로 반환
    }

}
