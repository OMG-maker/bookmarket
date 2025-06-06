package com.example.bookmarket.cart.service;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.cart.dto.CartResponseDTO;
import com.example.bookmarket.cart.entity.Cart;
import com.example.bookmarket.cart.repository.CartRepository;
import com.example.bookmarket.cartBook.entity.CartBook;
import com.example.bookmarket.cartBook.repository.CartBookRepository;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.bookmarket.common.ErrorMessages.BOOK_NOT_FOUND;
import static com.example.bookmarket.common.ErrorMessages.USER_NOT_FOUND;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartBookRepository cartBookRepository;

    public CartService(CartRepository cartRepository, BookRepository bookRepository, UserRepository userRepository, CartBookRepository cartBookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.cartBookRepository = cartBookRepository;
    }

    @Transactional
    public void addBookToCart(Long userId, Long bookId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));

        // 1. 해당 유저의 PENDING 상태 카트 찾기
        Cart cart = cartRepository.findByUserAndStatus(user, Cart.Status.PENDING)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().user(user).status(Cart.Status.PENDING).build()
                ));

        // 2. 이미 카트에 있는 책인지 확인
        Optional<CartBook> existing = cartBookRepository.findByCartAndBook(cart, book);

        if (existing.isPresent()) {
            // 수량 업데이트
            CartBook cartBook = existing.get();
            cartBook.setQuantity(cartBook.getQuantity() + quantity);
        } else {
            // 새로 추가
            CartBook newCartBook = CartBook.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(quantity)
                    .build();
            cartBookRepository.save(newCartBook);
        }
    }

    @Transactional
    public CartResponseDTO getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.Status.PENDING)
                .orElseGet(() -> Cart.builder() // orElseGet : 값이 없을 경우, 대체 값을 "지연 실행(lazy)"으로 생성해서 리턴.
                                                    // 🔹 언제 쓰나?
                                                        // 값이 없으면 대체 객체를 생성해서 써도 되는 상황.
                        .user(user)
                        .status(Cart.Status.PENDING)
                        .cartBooks(new ArrayList<>())
                        .build());

        return CartResponseDTO.fromEntity(cart);
    }

    @Transactional
    public void updateCartBookQuantity(Long userId, Long bookId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.Status.PENDING)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user"));

        CartBook cartBook = cartBookRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new IllegalStateException("Book not found in cart"));

        // 수량 업데이트
        cartBook.setQuantity(quantity);
    }

    @Transactional
    public void removeBookFromCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));

        Cart cart = cartRepository.findByUserAndStatus(user, Cart.Status.PENDING)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user"));

        CartBook cartBook = cartBookRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new IllegalStateException("Book not found in cart"));

        cartBookRepository.delete(cartBook);
    }


}
