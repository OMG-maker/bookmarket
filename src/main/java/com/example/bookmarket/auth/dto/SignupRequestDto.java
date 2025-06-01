package com.example.bookmarket.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SignupRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    // getter, setter
}
