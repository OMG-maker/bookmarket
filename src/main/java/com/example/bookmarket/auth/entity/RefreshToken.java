package com.example.bookmarket.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RefreshToken {
    @Id
    private String userId; // 👈 실제로는 email 저장

    @Column(nullable = false)
    private String token; // 실제 refresh token 값

    @Column(nullable = false)
    private Instant expiryDate; // 만료일시

    // 생성자, getter/setter 생략
}
