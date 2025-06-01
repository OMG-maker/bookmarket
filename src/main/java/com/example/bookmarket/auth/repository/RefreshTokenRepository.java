package com.example.bookmarket.auth.repository;

import com.example.bookmarket.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}

