package com.gov.grievance.grievance_portal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int statusCode;
    private LocalDateTime timestamp;
    private Object errors;

    public static <T> ApiResponse<T> success(
            T data, String message, int statusCode) {
        return new ApiResponse<>(
                true,
                message,
                data,
                statusCode,
                LocalDateTime.now(),
                null
        );
    }
    public static <T> ApiResponse<T> error(
            String message, int statusCode)
    {
        return new ApiResponse<>(
                false,
                message,
                null,
                statusCode,
                LocalDateTime.now(),
                null
        );
    }

    public static <T> ApiResponse<T> validationError
            (String message, Object errors){
        return new ApiResponse<>(
                false,
                message,
                null,
                400,
                LocalDateTime.now(),
                errors
        );
    }
}
