package com.example.bookmarket.review.repository;

import com.example.bookmarket.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
//    Optional<Review> findByBookAndUser(Book book, User user);
}
