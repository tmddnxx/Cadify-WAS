package com.cadify.cadifyWAS.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDTO {

    @Getter
    @NoArgsConstructor
    public static class LoginRequest{
        @NotNull
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        private String email;
        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
                message = "비밀번호는 대문자, 소문자, 특수문자(!@#$%^&*), 숫자를 포함하여 8자리 이상이여야 합니다."
        )
        private String password;
    }
    @Getter
    public static class RefreshTokenRequest{
        private String refreshToken;
    }
    @Builder
    @Getter
    public static class AuthResponse{
        private String refreshToken;
        private String accessToken;
    }
}
