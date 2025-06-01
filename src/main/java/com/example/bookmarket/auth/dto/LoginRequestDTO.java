package com.example.bookmarket.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginRequestDTO {
    private String username;
    private String password;
    private String email;
}
