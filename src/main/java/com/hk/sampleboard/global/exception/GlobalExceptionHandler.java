package com.hk.sampleboard.global.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        log.error("errorCode = {}, errorMessage = {}",e.getErrorCode(), e.getErrorMessage());
        return new ResponseEntity<>(errorResponse,e.getErrorCode().getStatusCode());
    }
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(CustomJwtException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        log.error("errorCode = {}, errorMessage = {}",e.getErrorCode(), e.getErrorMessage());
        return new ResponseEntity<>(errorResponse, ErrorCode.JWT_TOKEN_ERROR.getStatusCode());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ErrorResponse.ValidationError> validationErrors = fieldErrors.stream()
                .map(error -> new ErrorResponse.ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());


        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_INPUT)
                .errorMessage(ErrorCode.INVALID_INPUT.getDescription())
                .errors(validationErrors)
                .build();
        log.error("errorCode = {}, errorMessage = {}, errors={}" ,ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getDescription(), validationErrors);
        return new ResponseEntity<>(errorResponse, ErrorCode.INVALID_INPUT.getStatusCode());
    }
}
