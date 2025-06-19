package com.example.bookmarket.review.repository;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.review.entity.Review;
import com.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Optional<Review> findByBookAndUser(Book book, User user);
}
