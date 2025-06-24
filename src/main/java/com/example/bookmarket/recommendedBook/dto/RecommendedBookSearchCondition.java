package com.example.bookmarket.recommendedBook.dto;

import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBookSearchCondition {
    private RecommendedBook.Type type; // ENUM (필터용)
    private LocalDate fromDate;        // 생성일 범위 시작
    private LocalDate toDate;          // 생성일 범위 끝
}