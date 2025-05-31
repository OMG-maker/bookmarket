package com.example.bookmarket.user.dto;

import com.example.bookmarket.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private Long id;

    private String email;
    private String username;
    private String password;

    private LocalDateTime createdAt;

    private User.Role role;
    private Boolean isActive = true;

    /**
     * User 엔티티를 UserDTO로 변환하는 정적 메소드
     * @param user 변환할 User 엔티티
     * @return 변환된 UserDTO 객체
     */
    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }

    /**
     * UserDTO User 엔티티로 변환하는 메소드
     * @return 변환된 User 엔티티
     */
    public User toEntity() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .createdAt(this.createdAt)
                .role(this.role)
                .isActive(this.isActive)
                .build();

    }

}
