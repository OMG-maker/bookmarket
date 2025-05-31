package com.example.bookmarket.book.service;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.exception.BookAlreadyExistsException;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.common.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.BOOK_NOT_FOUND;

@Service
public class BookService {

    // BookRepository를 주입받기 위한 필드
    private final BookRepository bookRepository;

    // 생성자 주입을 통해 BookRepository를 주입받음
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
        // title, author 중복 체크
        bookRepository.findByTitleAndAuthor(dto.getTitle(), dto.getAuthor()).ifPresent(book -> { // title과 author로 중복된 책이 있는지 확인
            throw new BookAlreadyExistsException(ErrorMessages.BOOK_ALREADY_EXISTS); // 중복된 책이 있으면 예외 발생
        });

        Book saved = bookRepository.save(dto.toEntity()); // BookDTO 형태로 받아온 데이터를 Book 엔티티로 변환하여 저장
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
                .title(dto.getTitle()) // 사용자 이름 수정
                .author(dto.getAuthor()) // 비밀번호 수정
                .build()
        ));
    }

    // ID로 책이 존재하는지 확인하는 메소드
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}