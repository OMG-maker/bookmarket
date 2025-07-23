package com.example.bookmarket.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SignupRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,20}$",
            message = "닉네임은 한글/영문/숫자로 2~20자여야 합니다."
    )
    private String username;

    @NotBlank
    @Size(min = 6, max = 20, message = "비밀번호는 6~20자여야 합니다.") // 테스트 개발 기간에는 비밀번호에 길이 이상으론 제한을 두지 않음
    private String password;
}
