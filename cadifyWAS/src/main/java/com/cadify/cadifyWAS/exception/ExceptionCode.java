package com.cadify.cadifyWAS.exception;

import lombok.Getter;

public enum ExceptionCode {

    // MEMBER
    MEMBER_ALREADY_EXISTS(409, "이미 등록된 이메일 입니다."),
    MEMBER_NOT_FOUND(404, "등록된 이메일을 찾을 수 없습니다."),
    TOKEN_IS_EMPTY(401, "로그인이 필요한 서비스 입니다."),
    TOKEN_IS_EXPIRED(401, "재 로그인이 필요합니다."),
    INVALID_TOKEN(401, "인증정보가 올바르지 않습니다.");

    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
