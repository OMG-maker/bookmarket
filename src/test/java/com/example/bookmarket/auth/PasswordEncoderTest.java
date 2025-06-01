package com.example.bookmarket.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * PasswordEncoder를 사용하여 비밀번호를 암호화하고, 암호화된 비밀번호가 원본과 다르며
     * matches 메서드를 통해 일치하는지 확인하는 테스트.
     */
    @Test
    public void testPasswordEncoding() {
        String rawPassword = "1234"; // 테스트용 비밀번호
        String encodedPassword = passwordEncoder.encode(rawPassword); // 비밀번호 암호화

        assertNotEquals(rawPassword, encodedPassword); // 암호화됐는지 확인
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword)); // 일치 확인
    }
}