package com.example.bookmarket.user.repository;

import com.example.bookmarket.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 👈 이메일 중복 체크용

    Optional<User> findByUsername(String username); // 👈 사용자 이름으로 조회용
}
