package com.example.bookmarket.requestedBook.controller;

import com.example.bookmarket.requestedBook.service.RequestedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/admin/requested-books")
public class AdminRequestedBookController {

    private final RequestedBookService requestedBookService;

//    // admin 전용
//    // 희망 도서 요청을 승인하는 엔드포인트
//    @PostMapping("/{id}/approve")
//    public ResponseEntity<RequestedBookDTO> approveRequestedBook(@PathVariable Long id) {
//        RequestedBookDTO approvedRequestedBook = requestedBookService.approveRequestedBook(id); // ID로 희망 도서 요청 승인
//        return ResponseEntity.ok(approvedRequestedBook); // HTTP 200 OK 응답으로 반환
//    }

    // 희망 도서 요청을 거절하는 엔드포인트
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectRequestedBooks(@RequestBody List<Long> ids) {
        requestedBookService.rejectRequestedBooks(ids); // 희망 도서 요청을 거절
        return ResponseEntity.noContent().build(); // 204 반환
    }

}