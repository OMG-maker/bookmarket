package com.example.bookmarket.cartBook.repository;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.cart.entity.Cart;
import com.example.bookmarket.cartBook.entity.CartBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {
    Optional<CartBook> findByCartAndBook(Cart cart, Book book);

    List<CartBook> findByCart(Cart cart);
}
