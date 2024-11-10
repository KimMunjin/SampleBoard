package com.hk.sampleboard.global.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        log.error("MemberException is occurred", e.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse,e.getErrorCode().getStatusCode());
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.JWT_TOKEN_ERROR, e.getMessage());
        return new ResponseEntity<>(errorResponse, ErrorCode.JWT_TOKEN_ERROR.getStatusCode());
    }
}
