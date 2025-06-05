package com.example.bookmarket.book.dto;

import com.example.bookmarket.book.entity.Book;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDateTime publishedAt;
    private Integer stock;
    private BigDecimal price;
    private Boolean isActive;
    private Book.Status status;

    /**
     * Book 엔티티를 BookDTO로 변환하는 정적 메소드
     * @param book 변환할 Book 엔티티
     * @return 변환된 BookDTO 객체
     */
    public static BookDTO fromEntity(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishedAt(book.getPublishedAt())
                .stock(book.getStock())
                .price(book.getPrice())
                .isActive(book.getIsActive())
                .status(book.getStatus())
                .build();
        // ✅ Builder 방식 : 순서 상관없이 필드 설정 가능, 가독성 높고 안전함
    }

    /**
     * BookDTO를 Book 엔티티로 변환하는 메소드
     * @return 변환된 Book 엔티티
     */
    public Book toEntity() {
        return Book.builder()
                .id(this.id)
                .title(this.title)
                .author(this.author)
                .isbn(this.isbn)
                .publishedAt(this.publishedAt)
                .stock(this.stock)
                .price(this.price)
                .isActive(this.isActive != null ? this.isActive : true)
                .status(this.status != null ? this.status : Book.Status.IN_STOCK)
                .build();
    }
}