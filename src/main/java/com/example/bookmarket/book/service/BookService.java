package com.example.bookmarket.book.service;

import com.example.bookmarket.book.dto.BookDTO;
import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor()))
                .collect(Collectors.toList());
    }

    public BookDTO save(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());

        Book saved = bookRepository.save(book);
        return new BookDTO(saved.getId(), saved.getTitle(), saved.getAuthor());
    }
}