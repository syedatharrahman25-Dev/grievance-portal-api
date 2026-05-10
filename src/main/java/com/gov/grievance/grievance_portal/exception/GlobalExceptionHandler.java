package com.gov.grievance.grievance_portal.exception;

import com.gov.grievance.grievance_portal.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleBadRequestException(
            BadRequestException ex){
        ex.printStackTrace();
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidationException(
            MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        ApiResponse<Object> response =
                ApiResponse.validationError(
                        "Validation failed. Please check your input.",
                        errors
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleBadCredentialsException(
            BadCredentialsException ex){
        ApiResponse<Object> response = ApiResponse.error(
                "Invalid email or password. Please try again.",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>>
        HandleExpiredJwtException(
            ExpiredJwtException ex){
        ApiResponse<Object> response = ApiResponse.error(
                "Your Session has expired. Please login again.",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleJwtException(JwtException ex){
        ApiResponse<Object> response = ApiResponse.error(
                "Invalid authentication token.",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleRuntimeException(RuntimeException ex){
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleGlobalException(Exception ex){
        ApiResponse<Object> response = ApiResponse.error(
                "An unexpected error occurred. Please try again.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
