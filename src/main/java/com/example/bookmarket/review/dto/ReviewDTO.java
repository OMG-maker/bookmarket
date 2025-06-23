package com.example.bookmarket.review.dto;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.review.entity.Review;
import com.example.bookmarket.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewDTO {
    private Long id;
    private String content;
    private Long userId;
    private Long bookId;
    private LocalDateTime createdAt;

    /**
     * Review 엔티티를 ReviewDTO로 변환하는 정적 메소드
     * @param review 변환할 Review 엔티티
     * @return 변환된 ReviewDTO 객체
     */
    public static ReviewDTO fromEntity(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .createdAt(review.getCreatedAt())
                .build();
    }

    /**
     * ReviewDTO를 Review 엔티티로 변환하는 메소드
     * @return 변환된 Review 엔티티
     */
    public Review toEntity(User user, Book book) {
        return Review.builder()
                .id(this.id)
                .content(this.content)
                .user(user)
                .book(book)
                .createdAt(this.createdAt)
                .build();
    }

}
