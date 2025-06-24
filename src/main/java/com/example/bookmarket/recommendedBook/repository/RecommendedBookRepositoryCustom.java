package com.example.bookmarket.recommendedBook.repository;

import com.example.bookmarket.recommendedBook.dto.RecommendedBookSearchCondition;
import com.example.bookmarket.recommendedBook.entity.RecommendedBook;

import java.util.List;

public interface RecommendedBookRepositoryCustom {
    void deactivateRecommendedBooksByType(RecommendedBook.Type type);
    public List<RecommendedBook> findBySearchCondition(RecommendedBookSearchCondition condition);
}
