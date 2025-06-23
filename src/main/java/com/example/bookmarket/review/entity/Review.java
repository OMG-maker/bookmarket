package com.example.bookmarket.review.entity;

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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false) // DB 컬럼명 'is_active'
    @Builder.Default
    private Boolean isActive = true;

    // ▶ 저장 전에 호출되어 createdAt이 null이면 현재 시간으로 자동 세팅
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {            // null이면 true로 세팅
            this.createdAt = LocalDateTime.now();
        }
        if (this.isActive == null) {            // null이면 true로 세팅
            this.isActive = true;
        }
    }
}
