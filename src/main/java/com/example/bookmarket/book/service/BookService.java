package com.example.bookmarket.book.service;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.exception.BookAlreadyExistsException;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.requestedBook.entity.RequestedBook;
import com.example.bookmarket.requestedBook.repository.RequestedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.BOOK_ALREADY_EXISTS;
import static com.example.bookmarket.common.ErrorMessages.BOOK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookService {

    // BookRepository를 주입받기 위한 필드
    private final BookRepository bookRepository;
    private final RequestedBookRepository requestedBookRepository;

    // 모든 책을 조회하는 메소드
    public List<BookDTO> findAll() {
        return bookRepository.findAll() // 모든 책을 조회
                .stream() // 조회된 Book 엔티티 리스트를 스트림으로 변환
                .map(BookDTO::fromEntity) // 각 Book 엔티티를 BookDTO로 변환
                .toList(); // Java 16+에서 불변 리스트로 반환 // Java 8+에서는 collect(Collectors.toList())를 사용
    }

    // ID로 책을 조회하는 메소드
    public BookDTO findById(Long id) {
        // 해당 id의 책이 없으면 즉시 예외 발생하는 방식으로 변경
        Book book = bookRepository.findById(id) // ID로 책을 조회
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND)); // 만약 책이 존재하지 않으면 에러 반환
        return BookDTO.fromEntity(book); // 조회된 Book 엔티티를 BookDTO로 변환
    }

    // 책을 저장하는 메소드
    public BookDTO save(BookDTO dto) {
//        // title, author 중복 체크
//        bookRepository.findByTitleAndAuthor(dto.getTitle(), dto.getAuthor()).ifPresent(book -> { // title과 author로 중복된 책이 있는지 확인
//            throw new BookAlreadyExistsException(ErrorMessages.BOOK_ALREADY_EXISTS); // 중복된 책이 있으면 예외 발생
//        });

        // ISBN 중복 체크 (ISBN은 NOT NULL이므로 null 체크는 불필요)
        bookRepository.findByIsbn(dto.getIsbn()).ifPresent(book -> {
            throw new BookAlreadyExistsException(BOOK_ALREADY_EXISTS);
        });

        Book saved = bookRepository.save(dto.toEntity()); // BookDTO 형태로 받아온 데이터를 Book 엔티티로 변환하여 저장

        // 저장된 책 ISBN에 해당하는 요청 중 상태가 PENDING인 것들 모두 COMPLETED로 업데이트
        List<RequestedBook> pendingRequests = requestedBookRepository.findAllByIsbnAndStatus(saved.getIsbn(), RequestedBook.Status.PENDING);
        for (RequestedBook req : pendingRequests) {
            req.setStatus(RequestedBook.Status.COMPLETED);
        }
        requestedBookRepository.saveAll(pendingRequests);

        return BookDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // 책을 삭제하는 메소드
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) { // ID로 책이 존재하는지 확인
            throw new BookNotFoundException(BOOK_NOT_FOUND); // 만약 책이 존재하지 않으면 예외 발생
        }

        bookRepository.deleteById(id); // ID로 책을 삭제
    }

    // 책을 수정하는 메소드
    public BookDTO update(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id) // ID로 책 엔티티를 조회
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND)); // 만약 책이 존재하지 않으면 에러 반환

        // 수정된 결과를 BookDTO로 변환하여 반환
        return BookDTO.fromEntity(bookRepository.save(book.toBuilder() // toBuilder()를 사용해서 기존 user를 복사하면서 변경
                .title(dto.getTitle() != null ? dto.getTitle() : book.getTitle()) // 사용자 이름 수정
                .author(dto.getAuthor() != null ? dto.getAuthor() : book.getAuthor()) // 설명 수정
//                .isbn(dto.getIsbn()!= null ? dto.getIsbn() : book.getIsbn()) // 고유번호 수정
                .publishedAt(dto.getPublishedAt() != null ? dto.getPublishedAt() : book.getPublishedAt()) // 출판일 수정
                .stock(dto.getStock() != null ? dto.getStock() : book.getStock()) // 재고 수정
                .price(dto.getPrice() != null ? dto.getPrice() : book.getPrice()) // 가격 수정
//                .isActive(dto.getIsActive()) // 활성화 여부 수정
//                .status(dto.getStatus()) // 상태 수정
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : book.getIsActive())
                .status(dto.getStatus() != null ? dto.getStatus() : book.getStatus())
                .build()
        ));
    }

    // 제목과 저자 이름으로 책을 페이지 단위로 검색하는 메소드
    public Page<BookDTO> searchBooks(String title, String author, Pageable pageable) {
        return bookRepository
                .searchBooks(title, author, pageable)  // 👉 BookRepositoryImpl의 QueryDSL 메소드 호출됨
                .map(BookDTO::fromEntity);
    }

    // ID로 책이 존재하는지 확인하는 메소드
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

}