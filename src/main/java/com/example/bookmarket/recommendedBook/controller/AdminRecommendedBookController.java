package com.example.bookmarket.recommendedBook.controller;

import com.example.bookmarket.recommendedBook.dto.RecommendedBookDTO;
import com.example.bookmarket.recommendedBook.service.RecommendedBookService;
import com.example.bookmarket.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/admin/recommended-books")
public class AdminRecommendedBookController {

    private final RecommendedBookService recommendedBookService;
    private final UserService userService;

    // 현재 로그인한 관리자의 추천 도서를 등록하는 엔드포인트
    @PostMapping
    public ResponseEntity<RecommendedBookDTO> createRecommendedBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RecommendedBookDTO dto) {

        // 현재 로그인한 사용자의 email 가져오기
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        RecommendedBookDTO savedRecommendedBook = recommendedBookService.save(dto, userId);
        return ResponseEntity.ok(savedRecommendedBook);
    }

    // 추천 도서를 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<RecommendedBookDTO> updateRequestedBook(@PathVariable Long id, @RequestBody RecommendedBookDTO dto) {

        RecommendedBookDTO updatedRecommendedBook = recommendedBookService.update(dto, id); // ID로 희망 도서를 수정, 없으면 예외 던짐
        return ResponseEntity.ok(updatedRecommendedBook); // HTTP 200 OK 응답으로 반환
    }

    // 추천 도서를 ID로 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestedBook(@PathVariable Long id) {

        recommendedBookService.deleteById(id); // ID로 희망 도서를 삭제, 없으면 예외 던짐
        return ResponseEntity.noContent().build(); // 204 반환
    }

//    generateMonthlyBestSellerBooks()
    // 추천 도서의 월간 베스트셀러를 생성하는 엔드포인트
    @PostMapping("/generate-monthly-best-seller")
    public ResponseEntity<List<RecommendedBookDTO>> generateMonthlyBestSellerBooks(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

//        recommendedBookService.generateMonthlyBestSellerBooks(userId);
//        return ResponseEntity.ok().build(); // HTTP 200 OK 응답으로 반환

        List<RecommendedBookDTO> recommendedBooks = recommendedBookService.generateMonthlyBestSellerBooks(userId);
        return ResponseEntity.ok(recommendedBooks);
    }

}
