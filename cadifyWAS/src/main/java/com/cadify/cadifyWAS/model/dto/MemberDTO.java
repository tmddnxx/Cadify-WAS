package com.cadify.cadifyWAS.model.dto;


import com.cadify.cadifyWAS.model.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

public class MemberDTO {

    @Getter
    @NoArgsConstructor
    public static class Post{

        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^[가-힣]{2,8}$", message ="이름은 2이상 8이하의 한글이어여 합니다.")
        private String memberName;
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        private String email;
        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
                message = "비밀번호는 대문자, 소문자, 특수문자(!@#$%^&*), 숫자를 포함하여 8자리 이상이여야 합니다."
        )
        private String password;
        @NotNull
        private int addressNumber;
        @NotBlank
        private String addressDetail;
        @NotBlank
        private String phone;
        @NotNull
        private Member.Role memberRole;
        @NotNull
        private Map<Member.ConsentName, Member.ConsentStatus> consents;

        public Post replacePassword(String password){
            this.password = password;
            return this;
        }
    }
    @Getter
    @NoArgsConstructor
    public static class Patch{
        private String email;
        private String password;
        @Pattern(regexp = "^[가-힣]{2,8}$", message ="변경할 이름은 2이상 8이하의 한글이어야 합니다.")
        private String memberName;
        private Integer addressNumber;
        private String addressDetail;
        private Map<Member.ConsentName, Member.ConsentStatus> consents;
    }
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Response{
        private String memberName;
        private String email;
        private Member.Role memberRole;
        private Map<Member.ConsentName, Member.ConsentStatus> consents;
    }
}
