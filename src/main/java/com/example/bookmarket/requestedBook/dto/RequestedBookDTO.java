package com.example.bookmarket.requestedBook.dto;

import com.example.bookmarket.requestedBook.entity.RequestedBook;
import com.example.bookmarket.user.entity.User;
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
    private RequestedBook.Status status;

    /**
     * RequestedBook 엔티티를 RequestedBookDTO로 변환하는 정적 메소드
     *
     * @param requestedBook 변환할 RequestedBook 엔티티
     * @return 변환된 RequestedBookDTO 객체
     */
    // DB에서 가져온 Entity 객체를 클라이언트에 응답 보낼 때 사용.
    public static RequestedBookDTO fromEntity(RequestedBook requestedBook) {
        return RequestedBookDTO.builder()
                .id(requestedBook.getId())
                .userId(requestedBook.getUser() != null ? requestedBook.getUser().getId() : null)
                .title(requestedBook.getTitle())
                .author(requestedBook.getAuthor())
                .publisher(requestedBook.getPublisher())
                .isbn(requestedBook.getIsbn())
                .requestedAt(requestedBook.getRequestedAt())
                .status(requestedBook.getStatus())
                .build();
    }

    /**
     * RequestedBookDTO를 RequestedBook 엔티티로 변환하는 메소드
     *
     * @return 변환된 RequestedBook 엔티티
     */
    // 클라이언트가 JSON으로 보낸 DTO 요청을 DB 저장용 Entity로 변환할 때 사용.
    public RequestedBook toEntity(User user) {
        return RequestedBook.builder()
                .id(this.id)
                .user(user) // 외부에서 만들어서 주입했음
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .isbn(this.isbn)
                //      객체 생성 시 @PrePersist 에서 requestedAt, status 설정
                // .requestedAt(this.requestedAt)
                // .status(this.status != null ? this.status : RequestedBook.Status.PENDING)
                .build();
    }
}

//                .isActive(this.isActive != null ? this.isActive : true)
//                        .status(this.status != null ? this.status : Book.Status.IN_STOCK)