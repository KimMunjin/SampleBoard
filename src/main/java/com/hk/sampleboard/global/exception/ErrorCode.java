package com.hk.sampleboard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    INVALID_INPUT( HttpStatus.BAD_REQUEST,"잘못된 입력입니다."),
    //Member
    MEMBER_NOT_FOUNT(HttpStatus.NOT_FOUND,"회원을 찾을 수 없습니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"이메일이 이미 존재합니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"닉네임이 이미 존재합니다."),
    INVALID_EMAIL_PASSWORD(HttpStatus.BAD_REQUEST,"잘못된 이메일 혹은 비밀번호입니다."),
    LOGOUT_NOT_SUCCESSFUL(HttpStatus.BAD_REQUEST,"로그아웃 과정에서 문제가 발생했습니다."),
    FAIL_DELETE_MEMBER(HttpStatus.BAD_REQUEST,"회원 탈퇴 실패"),
    NO_AUTHORITY_ERROR(HttpStatus.FORBIDDEN,"권한이 없습니다."),

    //토큰 관련
    JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "JWT 토큰 에러입니다."),
    JWT_EXPIRED(HttpStatus.FORBIDDEN, "JWT가 만료되었습니다."),
    JWT_TOKEN_WRONG_TYPE(HttpStatus.FORBIDDEN,"JWT 토큰 형식에 문제가 생겼습니다."),
    JWT_TOKEN_MALFORMED(HttpStatus.FORBIDDEN,"토큰이 변조되었습니다."),
    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.FORBIDDEN,"Refresh 토큰을 찾을 수 없습니다.");

    private final HttpStatus statusCode;
    private final String description;


}