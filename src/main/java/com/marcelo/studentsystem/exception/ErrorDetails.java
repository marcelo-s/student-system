package com.marcelo.studentsystem.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Custom class for gathering argument validation errors
 */
@Data
public class ErrorDetails {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorDetails(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
