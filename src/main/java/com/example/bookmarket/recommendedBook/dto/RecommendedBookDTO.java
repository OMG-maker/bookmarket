package com.example.bookmarket.recommendedBook.dto;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import com.example.bookmarket.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RecommendedBookDTO {
    private Long id; // 추천 도서 ID
    private Long bookId; // 도서 정보
    private Long createdBy; // 추천도서 지정 관리자 user_id
    private LocalDateTime createdAt; // 추천 도서 생성 일시
    private RecommendedBook.Type type; // 추천 도서 타입 (MONTHLY, POPULAR)
    private RecommendedBook.Status status;
    private Integer displayOrder; // 추천 목록 내 순서

    /**
     * RecommendedBook 엔티티를 RecommendedBookDTO로 변환하는 정적 메소드
     *
     * @param recommendedBook 변환할 RecommendedBook 엔티티
     * @return 변환된 RecommendedBookDTO 객체
     */
    public static RecommendedBookDTO fromEntity(RecommendedBook recommendedBook) {
        return RecommendedBookDTO.builder()
                .id(recommendedBook.getId())
                .bookId(recommendedBook.getBook() != null ? recommendedBook.getBook().getId() : null)
                .createdBy(recommendedBook.getCreatedBy() != null ? recommendedBook.getCreatedBy().getId() : null)
                .createdAt(recommendedBook.getCreatedAt())
                .type(recommendedBook.getType())
                .status(recommendedBook.getStatus())
                .displayOrder(recommendedBook.getDisplayOrder())
                .build();
    }

    /**
     * RecommendedBookDTO를 RecommendedBook 엔티티로 변환하는 메소드
     *
     * @return 변환된 RecommendedBook 엔티티 객체
     */
    public RecommendedBook toEntity(User createdBy, Book book){
        return RecommendedBook.builder()
                .id(this.id)
                .book(book) // Book 엔티티는 외부에서 주입
                .createdBy(createdBy) // User 엔티티는 외부에서 주입
                .createdAt(this.createdAt)
                .type(this.type != null ? this.type : RecommendedBook.Type.MONTHLY_ADMIN_PICK) // 기본값 설정
                .status(this.status != null ? this.status : RecommendedBook.Status.ACTIVE) // 기본값 설정
                .displayOrder(this.displayOrder != null ? this.displayOrder : 0) // 기본값 설정
                .build();
    }
}
