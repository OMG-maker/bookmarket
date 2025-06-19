package com.example.bookmarket.review.repository;

import com.example.bookmarket.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> searchReviews(Long userId, Long bookId, Pageable pageable);
}