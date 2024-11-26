package com.hk.sampleboard.global.exception;

import io.jsonwebtoken.JwtException;
import lombok.*;

@Getter
@Setter
public class CustomJwtException extends JwtException {
    private ErrorCode errorCode;
    private String errorMessage;

    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
