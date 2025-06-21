package com.example.bookmarket.requestedBook.repository;

import com.example.bookmarket.requestedBook.entity.RequestedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestedBookRepository extends JpaRepository<RequestedBook, Long>, RequestedBookRepositoryCustom {
//     Optional<RequestedBook> findByIsbnAndStatus(String isbn, String status); // 👈 isbn과 status로 RequestedBook 조회
    List<RequestedBook> findAllByIsbnAndStatus(String isbn, RequestedBook.Status status);
}
