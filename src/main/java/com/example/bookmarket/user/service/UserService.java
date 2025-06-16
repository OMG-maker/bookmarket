package com.example.bookmarket.user.service;

import com.example.bookmarket.auth.dto.SignupRequestDto;
import com.example.bookmarket.user.dto.UserDTO;
import com.example.bookmarket.user.entity.User;
import com.example.bookmarket.user.exception.UserAlreadyExistsException;
import com.example.bookmarket.user.exception.UserNotFoundException;
import com.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bookmarket.common.ErrorMessages.USER_ALREADY_EXISTS;
import static com.example.bookmarket.common.ErrorMessages.USER_NOT_FOUND;

@Service
@EnableWebSecurity
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 모든 사용자를 조회하는 메소드
    public List<UserDTO> findAll() {
        return userRepository.findAll() // 모든 사용자 엔티티를 조회
                .stream()// 조회된 User 엔티티 리스트를 스트림으로 변환
                .map(UserDTO::fromEntity) // 각 User 엔티티를 UserDTO로 변환
                .toList(); // Java 16+에서 불변 리스트로 반환 // Java 8+에서는 collect(Collectors.toList())를 사용
    }

    // ID로 사용자를 조회하는 메소드
    public UserDTO findById(Long id) {
        // 해당 id의 유저가 없으면 즉시 예외 발생하는 방식으로 변경
        User user = userRepository.findById(id) // ID로 사용자 엔티티를 조회
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)); // 만약 사용자가 존재하지 않으면 에러 반환
        return UserDTO.fromEntity(user); // 조회된 User 엔티티를 UserDTO로 변환
    }

    // 사용자를 저장하는 메소드 (관리자용)
    public UserDTO save(UserDTO dto) {
        // email 중복 체크
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> { // 이메일로 중복된 사용자가 있는지 확인
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS); // 중복된 사용자가 있으면 예외 발생
        });

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User newUser = dto.toEntity().toBuilder()
                .password(encodedPassword)
                .role(User.Role.USER) // 기본 역할 부여 (Role.USER로 가정)
                .build();

        User saved = userRepository.save(newUser);

        return UserDTO.fromEntity(saved); // 저장한 결과값을 반환
    }

    // 사용자를 저장하는 메소드 (회원가입용)
    public UserDTO save(SignupRequestDto dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS);
        });

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(encodedPassword)
                .role(User.Role.USER) // ★ 기본 권한 설정
                .isActive(true)  // ★ 기본 활성화
                .build();

        User saved = userRepository.save(user);
        return UserDTO.fromEntity(saved);
    }

    // 사용자를 삭제하는 메소드
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) { // ID로 사용자가 존재하는지 확인
            throw new UserNotFoundException(USER_NOT_FOUND); // 만약 사용자가 존재하지 않으면 예외 발생
        }

        userRepository.deleteById(id); // ID로 사용자를 삭제
    }

    // 사용자를 수정하는 메소드
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id) // ID로 사용자 엔티티를 조회
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND)); // 만약 사용자가 존재하지 않으면 에러 반환

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 수정된 결과를 UserDTO로 변환하여 반환
        return UserDTO.fromEntity(userRepository.save(user.toBuilder() // toBuilder()를 사용해서 기존 user를 복사하면서 변경
                .username(dto.getUsername()) // 사용자 이름 수정
//                .password(dto.getPassword()) // 비밀번호 수정
                .password(encodedPassword) // 비밀번호 수정
                .role(dto.getRole()) // 역할 수정
                .isActive(dto.getIsActive()) // 활성화 상태 수정
                .build()
        ));
    }

    // ID로 사용자가 존재하는지 확인하는 메소드
    public boolean existsById(Long id) {
        return userRepository.existsById(id); // ID로 사용자가 존재하는지 확인
    }
}
