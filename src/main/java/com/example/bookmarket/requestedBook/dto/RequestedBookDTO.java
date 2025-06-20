package com.example.bookmarket.requestedBook.dto;

import com.example.bookmarket.requestedBook.entity.RequestedBook;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RequestedBookDTO {
    private Long id;
    private Long userId; // 요청한 사용자 ID
    private String title; // 책 제목
    private String author; // 저자
    private String publisher; // 출판사
    private String isbn;  // 고유번호
    private LocalDateTime requestedAt; // 구매 요청 날짜

    /**
     * RequestedBook 엔티티를 RequestedBookDTO로 변환하는 정적 메소드
     *
     * @param requestedBook 변환할 RequestedBook 엔티티
     * @return 변환된 RequestedBookDTO 객체
     */
    public static RequestedBookDTO fromEntity(RequestedBook requestedBook) {
        return RequestedBookDTO.builder()
                .id(requestedBook.getId())
                .userId(requestedBook.getUser() != null ? requestedBook.getUser().getId() : null)
                .title(requestedBook.getTitle())
                .author(requestedBook.getAuthor())
                .publisher(requestedBook.getPublisher())
                .isbn(requestedBook.getIsbn())
                .requestedAt(requestedBook.getRequestedAt())
                .build();
    }


    /**
     * RequestedBookDTO를 RequestedBook 엔티티로 변환하는 메소드
     *
     * @return 변환된 RequestedBook 엔티티
     */
    public RequestedBook toEntity() {
        return RequestedBook.builder()
                .id(this.id)
                .user(null) // User 엔티티는 별도로 설정해야 함
                // Service 에서 아래처럼 설정
                    // User user = userRepository.findById(this.userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                    // RequestedBook requestedBook = dto.toEntity();
                    // requestedBook.setUser(user);
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .isbn(this.isbn)
                .requestedAt(this.requestedAt)
                .build();
    }
}

//                .isActive(this.isActive != null ? this.isActive : true)
//                        .status(this.status != null ? this.status : Book.Status.IN_STOCK)