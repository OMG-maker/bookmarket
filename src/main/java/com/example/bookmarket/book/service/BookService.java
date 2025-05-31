package com.example.bookmarket.book.service;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
                //.collect(Collectors.toList());        // Java 8+, ArrayList (가변)
                .toList();      // 	Java 16+, 불변 리스트 (UnmodifiableList)
    }

    // ID로 책을 조회하는 메소드
    public BookDTO findById(Long id) {
        return bookRepository.findById(id) // ID로 책을 조회
                .map(BookDTO::fromEntity) // 조회된 Book 엔티티를 BookDTO로 변환
                .orElse(null); // 만약 책이 존재하지 않으면 null 반환
    }

    // 책을 저장하는 메소드
    public BookDTO save(BookDTO dto) {
        Book saved = bookRepository.save(dto.toEntity()); // BookDTO 형태로 받아온 데이터를 Book 엔티티로 변환하여 저장
        return BookDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // 책을 삭제하는 메소드
    public void deleteById(Long id) {
        bookRepository.deleteById(id); // ID로 책을 삭제
    }

    // 책을 수정하는 메소드
    public BookDTO update(Long id, BookDTO dto) {
        return bookRepository.findById(id) // ID로 책을 조회
                .map(book -> {
                    book.setTitle(dto.getTitle()); // 제목 수정
                    book.setAuthor(dto.getAuthor()); // 저자 수정
                    Book updated = bookRepository.save(book); // 수정된 엔티티 저장
                    return BookDTO.fromEntity(updated); // 수정된 결과를 BookDTO로 변환하여 반환
                })
                .orElse(null); // 만약 책이 존재하지 않으면 null 반환
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}