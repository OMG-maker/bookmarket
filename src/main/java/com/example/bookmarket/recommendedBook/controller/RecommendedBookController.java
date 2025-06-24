package com.example.bookmarket.recommendedBook.controller;

import com.example.bookmarket.recommendedBook.dto.RecommendedBookDTO;
import com.example.bookmarket.recommendedBook.dto.RecommendedBookSearchCondition;
import com.example.bookmarket.recommendedBook.service.RecommendedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommended-books")
public class RecommendedBookController {

    private final RecommendedBookService recommendedBookService;

    // 추천 도서 조회 엔드포인트
    @GetMapping
    public ResponseEntity<List<RecommendedBookDTO>> getAllRecommendedBooks(RecommendedBookSearchCondition condition) {
        // List<RecommendedBookDTO> recommendedBooks = recommendedBookService.findAll();
        // return ResponseEntity.ok(recommendedBooks);

        List<RecommendedBookDTO> books = recommendedBookService.findBySearchCondition(condition);
        return ResponseEntity.ok(books);
    }

    // ID로 추천 도서를 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<RecommendedBookDTO> getRecommendedBookById(@PathVariable Long id) {
        RecommendedBookDTO recommendedBook = recommendedBookService.findById(id); // ID로 희망 도서를 조회, 없으면 예외 던짐
        return ResponseEntity.ok(recommendedBook); // HTTP 200 OK 응답으로 반환
    }

}
