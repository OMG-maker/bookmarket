package com.example.bookmarket.requestedBook.repository;

import com.example.bookmarket.requestedBook.entity.RequestedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestedBookRepository extends JpaRepository<RequestedBook, Long>, RequestedBookRepositoryCustom {
}
