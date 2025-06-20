package com.example.bookmarket.review.controller;

import com.example.bookmarket.review.dto.ReviewDTO;
import com.example.bookmarket.review.service.ReviewService;
import com.example.bookmarket.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    // 생성자 주입을 통해 ReviewService를 주입받음
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    // 모든 리뷰를 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.findAll(); // 모든 리뷰를 조회
        return ResponseEntity.ok(reviews); // HTTP 200 OK 응답으로 반환
    }

    // ID로 리뷰를 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.findById(id); // ID로 리뷰를 조회, 없으면 예외 던짐
        return ResponseEntity.ok(review); // HTTP 200 OK 응답으로 반환
    }

    // 현재 로그인한 사용자의 리뷰를 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long bookId,
            @RequestBody ReviewDTO dto) {

        // 현재 로그인한 사용자의 email 가져오기
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        ReviewDTO savedReview = reviewService.save(dto, userId, bookId);
        return ResponseEntity.ok(savedReview);
    }

    // ID로 리뷰를 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        ReviewDTO updatedReview = reviewService.update(id, dto, userId); // ID로 리뷰를 수정, 없으면 예외 던짐
        return ResponseEntity.ok(updatedReview); // HTTP 200 OK 응답으로 반환
    }

    // ID로 리뷰를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        reviewService.deleteById(id, userId); // ID로 리뷰를 삭제, 없으면 예외 던짐
        return ResponseEntity.noContent().build(); // 204 반환
    }

    // 리뷰 페이징 서치
    @GetMapping("/search")
    public ResponseEntity<Page<ReviewDTO>> searchReviews(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> result = reviewService.searchReviews(userId, bookId, pageable);
        return ResponseEntity.ok(result);
    }

    // 내 리뷰 페이징 보기
    @GetMapping("/my-reviews")
    public ResponseEntity<Page<ReviewDTO>> myReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 현재 인증된 사용자의 이메일을 가져옵니다.
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> result = reviewService.searchReviews(userId, null, pageable);
        return ResponseEntity.ok(result);
    }
}
