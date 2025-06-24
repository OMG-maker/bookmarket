package com.example.bookmarket.recommendedBook.entity;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RecommendedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // 추천도서 지정 관리자 user_id

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(length = 100, nullable = false)
    private Integer displayOrder; // 추천 목록 내 순서

    public enum Type {
        MONTHLY_ADMIN_PICK, // 월별 관리자 지정
        MONTHLY_BEST_SELLER,    // 지난달 판매순으로 자동 지정
        WEEKLY_BEST_SELLER,     // 최근 7일 판매순 (추후 확장)
        YEARLY_BEST_SELLER      // 연간 인기 판매순 (추후 확장)
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

}
