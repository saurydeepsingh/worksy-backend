package com.worksy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> baseBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return body;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(baseBody(HttpStatus.NOT_FOUND, ex.getMessage(), null));
    }

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<Object> handleInvalidCredentials(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(baseBody(HttpStatus.UNAUTHORIZED, ex.getMessage(), null));
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<Object> handleDuplicateApplication(DuplicateApplicationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(baseBody(HttpStatus.CONFLICT, ex.getMessage(), null));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(baseBody(HttpStatus.CONFLICT, ex.getMessage(), null));
    }

    @ExceptionHandler({UnauthorizedException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleUnauthorized(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(baseBody(HttpStatus.FORBIDDEN, ex.getMessage(), null));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Object> handleFileUpload(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(baseBody(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "Validation failed", null);
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(baseBody(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseBody(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage(), null));
    }
}
