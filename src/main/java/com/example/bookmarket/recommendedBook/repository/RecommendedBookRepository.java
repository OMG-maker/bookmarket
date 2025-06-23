package com.example.bookmarket.recommendedBook.repository;

import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long>  {
}
