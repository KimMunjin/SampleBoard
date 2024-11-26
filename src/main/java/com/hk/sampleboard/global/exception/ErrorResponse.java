package com.hk.sampleboard.global.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
    private List<ValidationError> errors;

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    @Getter
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private Object rejectedValue;
        private String message;

    }
}
