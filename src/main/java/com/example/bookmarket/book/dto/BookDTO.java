package com.example.bookmarket.book.dto;

import com.example.bookmarket.book.entity.Book;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookDTO {

    private Long id;
    private String title;
    private String author;

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
                .build();
    }
}