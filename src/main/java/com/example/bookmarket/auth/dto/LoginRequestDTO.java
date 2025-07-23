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
public class LoginRequestDTO {

    // 로그인 시 아이디 사용 X
    private String username;

    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank
    @Size(min = 6, max = 20, message = "비밀번호는 6~20자여야 합니다.")
//    @Pattern( // 테스트 개발 기간에는 비밀번호에 길이 이상으론 제한을 두지 않음
//            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
//            message = "비밀번호는 영문과 숫자를 포함한 8~20자여야 합니다."
//    )
    private String password;

}
