package com.hk.sampleboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Member
    MEMBER_NOT_FOUNT(HttpStatus.NOT_FOUND,"회원을 찾을 수 없습니다."),

    //토큰 관련
    JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "JWT 토큰 에러입니다."),
    JWT_EXPIRED(HttpStatus.FORBIDDEN, "JWT가 만료되었습니다."),
    JWT_TOKEN_WRONG_TYPE(HttpStatus.FORBIDDEN,"JWT 토큰 형식에 문제가 생겼습니다."),
    JWT_TOKEN_MALFORMED(HttpStatus.FORBIDDEN,"토큰이 변조되었습니다."),
    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.FORBIDDEN,"Refresh 토큰을 찾을 수 없습니다.");

    private final HttpStatus statusCode;
    private final String description;


}