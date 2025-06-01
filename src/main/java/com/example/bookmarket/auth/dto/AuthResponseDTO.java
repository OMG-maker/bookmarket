package com.example.bookmarket.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType;
//    private String tokenType = "Bearer";
    private String refreshToken;
}
