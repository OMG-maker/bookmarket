package com.example.bookmarket.review.service;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.review.dto.ReviewDTO;
import com.example.bookmarket.review.entity.Review;
import com.example.bookmarket.review.exception.ReviewNotFoundException;
import com.example.bookmarket.review.repository.ReviewRepository;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.*;

@Service
@EnableWebSecurity
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // 모든 리뷰를 조회하는 메소드
    public List<ReviewDTO> findAll() {

        return reviewRepository.findAll() // 모든 엔티티를 조회
                .stream()// 조회된 Review 엔티티 리스트를 스트림으로 변환
                .map(ReviewDTO::fromEntity) // 각 Review 엔티티를 ReviewDTO로 변환
                .toList(); // Java 16+에서 불변 리스트로 반환 // Java 8+에서는 collect(Collectors.toList())를 사용

    }

    // ID로 리뷰를 조회하는 메소드
    public ReviewDTO findById(Long id) {
        return reviewRepository.findById(id) // ID로 리뷰 엔티티를 조회
                .map(ReviewDTO::fromEntity) // 조회된 Review 엔티티를 ReviewDTO로 변환
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND)); // 만약 리뷰가 존재하지 않으면 예외 발생
    }

    // 리뷰를 저장하는 메소드
    public ReviewDTO save(ReviewDTO dto, Long userId, Long bookId) { // (dto, userId, bookId)
        // 로그인한 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        // 책 찾기
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));

        // 이전 toEntity() 방법
//         DTO → Entity 변환
//        Review review = dto.toEntity();
//        review.setUser(user);
//        review.setBook(book);

        // 수정된 toEntity() 방법
        // DTO → Entity 변환
        Review review = dto.toEntity(user, book);

        // build() 방식도 가능하지만 성능상, 코드상 이득 없음. 가독성도 별로 안 좋아짐.
        Review saved = reviewRepository.save(review); // BookDTO 형태로 받아온 데이터를 Book 엔티티로 변환하여 저장
        return ReviewDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // ID로 리뷰를 삭제하는 메소드
    public void deleteById(Long id, Long userId) {
        Review review = reviewRepository.findById(id) // ID로 리뷰 엔티티를 조회
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND)); // 만약 리뷰가 존재하지 않으면 에러 반환

        // 리뷰 작성자가 요청한 사용자와 일치하는지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(REVIEW_NOT_OWNED);
        }

        reviewRepository.deleteById(id); // ID로 리뷰를 삭제
    }

    // ID로 리뷰를 수정하는 메소드
    public ReviewDTO update(Long id, ReviewDTO dto, Long userId) {
        Review review = reviewRepository.findById(id) // ID로 리뷰 엔티티를 조회
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND)); // 만약 리뷰가 존재하지 않으면 에러 반환

        // 리뷰 작성자가 요청한 사용자와 일치하는지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new AccessDeniedException(REVIEW_NOT_OWNED);
        }

        // 수정된 결과를 ReviewDTO 변환하여 반환
        return ReviewDTO.fromEntity(reviewRepository.save(review.toBuilder() // toBuilder()를 사용해서 기존 user를 복사하면서 변경
                .content(dto.getContent() != null ? dto.getContent() : review.getContent()) // 리뷰 내용 수정
                // 리뷰 id, 사용자 id, 책 id, 작성 시간 수정은 일반적으로 하지 않음
                .build()
        ));
    }

    // userId, bookId로 리뷰를 페이지 단위로 검색하는 메소드
    public Page<ReviewDTO> searchReviews(Long userId, Long bookId, Pageable pageable) {
        return reviewRepository
                .searchReviews(userId, bookId, pageable)  // 👉 ReviewRepositoryImpl의 QueryDSL 메소드 호출됨
                .map(ReviewDTO::fromEntity);
    }
}
