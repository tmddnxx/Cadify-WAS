package com.cadify.cadifyWAS.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

public class MemberDTO {
    @Getter
    public static class Post{
        private String memberName;
        private String email;
        private String password;
    }
    @Getter
    public static class Patch{
        private String email;
        private String password;
        private String memberName;
    }
    @Builder
    public static class Response{
        private String memberName;
        private String email;
    }
}
