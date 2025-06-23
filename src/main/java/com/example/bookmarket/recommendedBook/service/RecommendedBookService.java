package com.example.bookmarket.recommendedBook.service;

import com.example.bookmarket.book.entity.Book;
import com.example.bookmarket.book.exception.BookNotFoundException;
import com.example.bookmarket.book.repository.BookRepository;
import com.example.bookmarket.recommendedBook.dto.RecommendedBookDTO;
import com.example.bookmarket.recommendedBook.entity.RecommendedBook;
import com.example.bookmarket.recommendedBook.exception.RecommendedBookNotFoundException;
import com.example.bookmarket.recommendedBook.repository.RecommendedBookRepository;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
@Transactional
public class RecommendedBookService {

    private final RecommendedBookRepository recommendedBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 모든 추천 도서를 조회하는 메소드
    public List<RecommendedBookDTO> findAll() {
        return recommendedBookRepository.findAll() // 모든 엔티티를 조회
                .stream()// 조회된 RecommendedBook 엔티티 리스트를 스트림으로 변환
                .map(RecommendedBookDTO::fromEntity) // 각 RecommendedBook 엔티티를 RecommendedBookDTO 변환
                .toList(); // Java 16+에서 불변 리스트로 반환 // Java 8+에서는 collect(Collectors.toList())를 사용
    }

    // ID로 추천 도서를 조회하는 메소드
    public RecommendedBookDTO findById(Long id) {
        return recommendedBookRepository.findById(id) // ID로 추천 도서 엔티티를 조회
                .map(RecommendedBookDTO::fromEntity) // 조회된 RecommendedBook 엔티티를 RecommendedBookDTO 로 변환
                .orElseThrow(() -> new RecommendedBookNotFoundException(RECOMMENDED_BOOK_NOT_FOUND)); // 만약 추천 도서가 존재하지 않으면 예외 발생
    }

    // 추천 도서를 저장하는 메소드 (현재 관리자 전용)
    public RecommendedBookDTO save(RecommendedBookDTO dto, Long userId) {
        // 로그인한 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        // 추천 도서의 대상인 책 찾기
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));

        // 수정된 toEntity() 방법
        // DTO → Entity 변환  // 클라이언트에서 받아온 DTO정보에는 사용자 정보가 없으므로, 위에서 userId로 찾은 User를 설정해야 함.
        RecommendedBook requestedBook = dto.toEntity(user, book);

        RecommendedBook saved = recommendedBookRepository.save(requestedBook); // RequestedBookDTO 형태로 받아온 데이터를 RequestedBook 엔티티로 변환하여 저장
        return RecommendedBookDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // 추천 도서를 수정하는 메소드 (현재 관리자 전용)
    public RecommendedBookDTO update(RecommendedBookDTO dto, Long id) {
        RecommendedBook recommendedBook = recommendedBookRepository.findById(id)
                .orElseThrow(() -> new RecommendedBookNotFoundException(RECOMMENDED_BOOK_NOT_FOUND));

        // 수정된 결과를 RequestedBookDTO 변환하여 반환
        return RecommendedBookDTO.fromEntity(recommendedBookRepository.save(recommendedBook.toBuilder()
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : recommendedBook.getDisplayOrder()) // DisplayOrder 수정
                .status(dto.getStatus() != null ? dto.getStatus() : recommendedBook.getStatus()) // Status 수정
                .build()
        ));
    }

    // 추천 도서를 삭제하는 메소드 (현재 관리자 전용)
    public void deleteById(Long id) {
        if (!recommendedBookRepository.existsById(id)) {
            throw new RecommendedBookNotFoundException(RECOMMENDED_BOOK_NOT_FOUND);
        }
        recommendedBookRepository.deleteById(id); // ID로 추천 도서 삭제
    }



}
