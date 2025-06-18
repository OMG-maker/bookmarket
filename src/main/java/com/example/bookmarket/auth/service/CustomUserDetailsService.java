package com.example.bookmarket.auth.service;

import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.bookmarket.common.ErrorMessages.EMAIL_NOT_FOUND;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    /**
//     * 사용자 이름으로 사용자를 조회하여 UserDetails 객체를 반환합니다.
//     *
//     * @param username 사용자 이름
//     * @return UserDetails 객체
//     * @throws UsernameNotFoundException 사용자 이름이 존재하지 않을 경우 예외 발생
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username) // 사용자 이름으로 User 엔티티를 조회
//                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음")); // 사용자가 존재하지 않으면 예외 발생
//
//        return org.springframework.security.core.userdetails.User // Spring Security의 UserDetails 구현체를 생성
//                .withUsername(user.getUsername()) // 사용자 이름 설정
//                .password(user.getPassword())  // passwordEncoder 적용된 값
//                .roles(user.getRole().name()) // USER, ADMIN 등
//                .build(); // UserDetails 객체 반환
//    }

    /**
     * 현재 인증된 사용자의 정보를 가져옵니다.
     * email을 기반으로 사용자 조회.
     *
     * @return 현재 인증된 User 엔티티
     */
    @Override
    public UserDetails loadUserByUsername(String email) { // 'username' = email로 동작
        User user = userRepository.findByEmail(email)    // email로 실제 조회
                .orElseThrow(() -> new UsernameNotFoundException(EMAIL_NOT_FOUND));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // email을 'username' 위치에 넣음 (principal 값)
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
//
//        return new CustomUserDetails(user); // 직접 생성
//    }

}