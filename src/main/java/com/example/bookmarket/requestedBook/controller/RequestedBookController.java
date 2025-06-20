package com.example.bookmarket.requestedBook.controller;

import com.example.bookmarket.requestedBook.dto.RequestedBookDTO;
import com.example.bookmarket.requestedBook.service.RequestedBookService;
import com.example.bookmarket.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/requested-books")
public class RequestedBookController {

    private final RequestedBookService requestedBookService;
    private final UserService userService;

    public RequestedBookController(RequestedBookService requestedBookService, UserService userService) {
        this.requestedBookService = requestedBookService;
        this.userService = userService;
    }

    // 모든 희망 도서를 조회하는 엔드포인트
    @GetMapping
    public ResponseEntity<List<RequestedBookDTO>> getAllRequestedBooks() {
        List<RequestedBookDTO> requestedBooks = requestedBookService.findAll(); // 모든 희망 도서를 조회
        return ResponseEntity.ok(requestedBooks); // HTTP 200 OK 응답으로 반환
    }

    // ID로 희망 도서를 조회하는 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<RequestedBookDTO> getRequestedBooksById(@PathVariable Long id) {
        RequestedBookDTO requestedBook = requestedBookService.findById(id); // ID로 희망 도서를 조회, 없으면 예외 던짐
        return ResponseEntity.ok(requestedBook); // HTTP 200 OK 응답으로 반환
    }

    // 현재 로그인한 사용자의 희망 도서를 생성하는 엔드포인트
    @PostMapping
    public ResponseEntity<RequestedBookDTO> createRequestedBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RequestedBookDTO dto) {

        // 현재 로그인한 사용자의 email 가져오기
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        RequestedBookDTO savedRequestedBook = requestedBookService.save(dto, userId);
        return ResponseEntity.ok(savedRequestedBook);
    }

    // ID로 희망 도서를 수정하는 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<RequestedBookDTO> updateRequestedBook(@PathVariable Long id, @RequestBody RequestedBookDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        RequestedBookDTO updatedRequestedBook = requestedBookService.update(id, dto, userId); // ID로 희망 도서를 수정, 없으면 예외 던짐
        return ResponseEntity.ok(updatedRequestedBook); // HTTP 200 OK 응답으로 반환
    }

    // ID로 희망 도서를 삭제하는 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestedBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        requestedBookService.deleteById(id, userId); // ID로 희망 도서를 삭제, 없으면 예외 던짐
        return ResponseEntity.noContent().build(); // 204 반환
    }

    // 희망 도서 페이징 서치
    @GetMapping("/search")
    public ResponseEntity<Page<RequestedBookDTO>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RequestedBookDTO> result = requestedBookService.searchRequestedBooks(userId, title, author, isbn, publisher, startDate, endDate, pageable);

        return ResponseEntity.ok(result);
    }

    // 내 희망 도서 페이징 보기
    @GetMapping("/my-requested-books")
    public ResponseEntity<Page<RequestedBookDTO>> myRequestedBooks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 현재 인증된 사용자의 이메일을 가져옵니다.
        String userEmail = userDetails.getUsername();
        Long userId = userService.findByEmail(userEmail).getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<RequestedBookDTO> result = requestedBookService.searchRequestedBooks(userId, title, author, isbn, publisher, startDate, endDate, pageable);

        return ResponseEntity.ok(result);
    }

}
