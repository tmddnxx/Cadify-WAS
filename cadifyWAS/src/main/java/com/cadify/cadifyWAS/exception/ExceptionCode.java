package com.cadify.cadifyWAS.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // Member
    MEMBER_ALREADY_EXISTS(409, "이미 등록된 이메일 입니다."),
    MEMBER_NOT_FOUND(404, "등록된 이메일을 찾을 수 없습니다."),
    INVALID_LOGIN_INFO(400, "로그인 정보가 올바르지 않은 형식입니다."),
    WRONG_LOGIN_INFO(404, "아이디 혹은 비밀번호가 틀렸습니다."),
    WRONG_PASSWORD(404, "비밀번호가 올바르지 않습니다."),
//    // Token
    REFRESH_TOKEN_EXPIRED(401, "재 로그인이 필요합니다."),
    INVALID_TOKEN(401, "유효하지 않은 인증정보 입니다.");

    private final int status;
    private final String message;

    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
