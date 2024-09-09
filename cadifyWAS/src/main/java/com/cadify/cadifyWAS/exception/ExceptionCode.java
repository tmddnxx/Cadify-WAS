package com.cadify.cadifyWAS.exception;

import lombok.Getter;

public enum ExceptionCode {

    // MEMBER
    MEMBER_ALREADY_EXISTS(409, "이미 등록된 이메일 입니다."),
    MEMBER_NOT_FOUND(404, "등록된 이메일을 찾을 수 없습니다.");

    private int status;
    @Getter
    private String message;
    ExceptionCode(int code, String message){
        this.status = code;
        this.message = message;
    }
}
