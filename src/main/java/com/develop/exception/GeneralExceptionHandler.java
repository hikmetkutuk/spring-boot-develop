package com.develop.exception;

import com.develop.dto.HttpResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<?> handle(UserRegistrationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<?> handle(UserLoginException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserRetrievalException.class)
    public ResponseEntity<?> handle(UserRetrievalException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, DataIntegrityViolationException.class})
    public ResponseEntity<HttpResponse> handle(RuntimeException ex) {
        String message = (ex instanceof EmailAlreadyExistsException) ? ex.getMessage() : "Email already in use";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message(message)
                        .path("/api/v1/user/register")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<HttpResponse> handle(UserUpdateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message(ex.getMessage())
                        .path("/api/v1/user/update")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .build()
        );
    }
}
