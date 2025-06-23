package com.example.bookmarket.requestedBook.service;

import com.example.bookmarket.book.exception.BookAlreadyExistsException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.requestedBook.dto.RequestedBookDTO;
import com.example.bookmarket.requestedBook.entity.RequestedBook;
import com.example.bookmarket.requestedBook.exception.RequestedBookNotFoundException;
import com.example.bookmarket.requestedBook.repository.RequestedBookRepository;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bookmarket.common.ErrorMessages.*;


@Service
@RequiredArgsConstructor
@EnableWebSecurity
@Transactional
public class RequestedBookService {

    private final RequestedBookRepository requestedBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 모든 희망 도서를 조회하는 메소드
    public List<RequestedBookDTO> findAll() {
        return requestedBookRepository.findAll() // 모든 엔티티를 조회
                .stream()// 조회된 RequestedBook 엔티티 리스트를 스트림으로 변환
                .map(RequestedBookDTO::fromEntity) // 각 RequestedBook 엔티티를 RequestedBookDTO로 변환
                .toList(); // Java 16+에서 불변 리스트로 반환 // Java 8+에서는 collect(Collectors.toList())를 사용
    }

    // ID로 희망 도서를 조회하는 메소드
    public RequestedBookDTO findById(Long id) {
        return requestedBookRepository.findById(id) // ID로 희망 도서 엔티티를 조회
                .map(RequestedBookDTO::fromEntity) // 조회된 RequestedBook 엔티티를 RequestedBookDTO로 변환
                .orElseThrow(() -> new RequestedBookNotFoundException(REQUESTED_BOOK_NOT_FOUND)); // 만약 희망 도서가 존재하지 않으면 예외 발생
    }

    // 희망 도서를 저장하는 메소드 (현재 로그인한 사용자 전용)
    public RequestedBookDTO save(RequestedBookDTO dto, Long userId) {
        // 로그인한 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        // 📌 매장(Book) 테이블에 이미 있는 ISBN인지 확인
        if (bookRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new BookAlreadyExistsException(BOOK_ALREADY_EXISTS);
        }

        // 이전 toEntity() 방법
        // DTO → Entity 변환  // 클라이언트에서 받아온 DTO정보에는 사용자 정보가 없으므로, 위에서 userId로 찾은 User를 설정해야 함.
        //        RequestedBook requestedBook = dto.toEntity();
        //        requestedBook.setUser(user);

        // 수정된 toEntity() 방법
        // DTO → Entity 변환  // 클라이언트에서 받아온 DTO정보에는 사용자 정보가 없으므로, 위에서 userId로 찾은 User를 설정해야 함.
        RequestedBook requestedBook = dto.toEntity(user);

        RequestedBook saved = requestedBookRepository.save(requestedBook); // RequestedBookDTO 형태로 받아온 데이터를 RequestedBook 엔티티로 변환하여 저장
        return RequestedBookDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // 자신의 희망 도서를 ID로 삭제하는 메소드
    public void deleteById(Long id, Long userId) {
        RequestedBook requestedBook = requestedBookRepository.findById(id) // ID로 리뷰 엔티티를 조회
                .orElseThrow(() -> new RequestedBookNotFoundException(REQUESTED_BOOK_NOT_FOUND)); // 만약 리뷰가 존재하지 않으면 에러 반환

        // 리뷰 작성자가 요청한 사용자와 일치하는지 확인
        if (!requestedBook.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(REQUESTED_BOOK_NOT_OWNED);
        }

        requestedBookRepository.deleteById(id); // ID로 리뷰를 삭제
    }

    // 자신의 희망 도서를 ID로 수정하는 메소드
    public RequestedBookDTO update(Long id, RequestedBookDTO dto, Long userId) {
        RequestedBook requestedBook = requestedBookRepository.findById(id) // ID로 희망 도서를 엔티티를 조회
                .orElseThrow(() -> new RequestedBookNotFoundException(REQUESTED_BOOK_NOT_FOUND)); // 만약 희망 도서가 존재하지 않으면 에러 반환

        // 희망 도서 작성자가 요청한 사용자와 일치하는지 확인
        if (!requestedBook.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(REQUESTED_BOOK_NOT_OWNED);
        }

        // 수정된 결과를 RequestedBookDTO 변환하여 반환
        return RequestedBookDTO.fromEntity(requestedBookRepository.save(requestedBook.toBuilder()
                .title(dto.getTitle() != null ? dto.getTitle() : requestedBook.getTitle()) // 희망 도서 제목 수정
                .author(dto.getAuthor() != null ? dto.getAuthor() : requestedBook.getAuthor()) // 희망 도서 작성자 수정
                .publisher(dto.getPublisher() != null ? dto.getPublisher() : requestedBook.getPublisher()) // 출판사 수정
                .isbn(dto.getIsbn() != null ? dto.getIsbn() : requestedBook.getIsbn()) // ISBN 수정
                .build()
        ));

        // toBuilder() 메소드를 사용하지 않고 수정하는 방법 예시
//        RequestedBook.RequestedBookBuilder builder = requestedBook.toBuilder();
//
//        // 필요한 필드들 전부 체크
//        if (dto.getTitle() != null) builder.title(dto.getTitle());
//        if (dto.getAuthor() != null) builder.author(dto.getAuthor());
//        if (dto.getPublisher() != null) builder.publisher(dto.getPublisher());
//        if (dto.getIsbn() != null) builder.isbn(dto.getIsbn());
//
//        return RequestedBookDTO.fromEntity(requestedBookRepository.save(builder.build()));
//
//        // 수정된 결과를 RequestedBookDTO 변환하여 반환
//        return RequestedBookDTO.fromEntity(requestedBookRepository.save(requestedBook.toBuilder()
//                .title(dto.getTitle() != null ? dto.getTitle() : requestedBook.getTitle()) // 희망 도서 제목 수정
//                .author(dto.getAuthor() != null ? dto.getAuthor() : requestedBook.getAuthor()) // 희망 도서 작성자 수정
//                .publisher(dto.getPublisher() != null ? dto.getPublisher() : requestedBook.getPublisher()) // 출판사 수정
//                .isbn(dto.getIsbn() != null ? dto.getIsbn() : requestedBook.getIsbn()) // ISBN 수정
//                .build()
//        ));
    }

    // RequestedBook 필드들 기준으로 희망 도서를 페이지 단위로 검색하는 메소드
    public Page<RequestedBookDTO> searchRequestedBooks(Long userId, String title, String author, String isbn, String publisher, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return requestedBookRepository
                .searchRequestedBooks(userId, title, author, isbn, publisher, startDate, endDate, pageable) // 페이지 단위로 희망 도서를 검색
                .map(RequestedBookDTO::fromEntity);
    }

    /**
     * 여러 희망 도서 요청을 일괄 반려 처리
     * @param ids 반려할 RequestedBook ID 리스트
     * @return 반려 처리된 RequestedBookDTO 리스트
     */
    public List<RequestedBookDTO> rejectRequestedBooks(List<Long> ids) {
        List<RequestedBook> requestedBooks = requestedBookRepository.findAllById(ids);

        for (RequestedBook rb : requestedBooks) {
            rb.setStatus(RequestedBook.Status.REJECTED);
        }

        List<RequestedBook> updated = requestedBookRepository.saveAll(requestedBooks);

        return updated.stream()
                .map(RequestedBookDTO::fromEntity)
                .collect(Collectors.toList());
    }

//    // 승인 상태로 변경 - 예시 1
//    public RequestedBookDTO approveRequestedBook(Long id) {
//        RequestedBook requestedBook = requestedBookRepository.findById(id)
//                .orElseThrow(() -> new RequestedBookNotFoundException(REQUESTED_BOOK_NOT_FOUND));
//
//        // 이미 있는 ISBN인 경우 REJECTED 설정
//        if (bookRepository.findByIsbn(requestedBook.getIsbn()).isPresent()) {
//            requestedBook.setStatus(RequestedBook.Status.REJECTED);
//        } else{
//            requestedBook.setStatus(RequestedBook.Status.COMPLETED);
//        }
//        RequestedBook updated = requestedBookRepository.save(requestedBook);
//        return RequestedBookDTO.fromEntity(updated);
//    }

//    // 승인 상태로 변경 - 예시 2
//    public RequestedBookDTO approveRequestedBook(Long id) {
//        RequestedBook requestedBook = requestedBookRepository.findById(id)
//                .orElseThrow(() -> new RequestedBookNotFoundException(REQUESTED_BOOK_NOT_FOUND));
//
//        String isbn = requestedBook.getIsbn();
//
//        // 이미 있는 ISBN인 경우 REJECTED 설정
//        if (bookRepository.findByIsbn(requestedBook.getIsbn()).isPresent()) {
//            requestedBook.setStatus(RequestedBook.Status.REJECTED);
//        } else{
//            requestedBook.setStatus(RequestedBook.Status.COMPLETED);
//        }
//
//        // 책 입고 및 승인 처리 등 비즈니스 로직 수행
//
//        // 같은 ISBN, 상태가 PENDING인 모든 요청 찾기
//        List<RequestedBook> pendingRequests = requestedBookRepository.findAllByIsbnAndStatus(isbn, RequestedBook.Status.PENDING);
//
//        // 모두 상태 COMPLETED로 변경
//        pendingRequests.forEach(r -> r.setStatus(RequestedBook.Status.COMPLETED));
//
//        // 일괄 저장
//        requestedBookRepository.saveAll(pendingRequests);
//    }

}
