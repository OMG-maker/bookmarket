package com.example.bookmarket.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, length = 20)
    private String isbn;  // 고유번호

    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        IN_STOCK, OUT_OF_STOCK, DISCONTINUED
    }

    // ▶ 저장 전에 호출되어 publishedAt이 null이면 현재 시간으로 자동 세팅
    @PrePersist
    public void prePersist() {
        if (publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }
}

//
//    Table book {
//        id integer [primary key]
//        title varchar [not null]
//        author varchar [not null]
//        isbn varchar(20) [unique] // 고유번호
//        published_at timestamp
//        stock integer [not null]
//        price decimal(10, 2) [not null]
//        is_active boolean [default: true]
//        status enum('in_stock', 'out_of_stock', 'discontinued') [default: 'in_stock']
//        }