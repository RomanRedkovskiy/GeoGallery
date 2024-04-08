package com.example.userservice.rest.provider;

import com.example.userservice.util.ExceptionResponse;
import com.example.userservice.util.exceptions.AuthenticationExecutionException;
import com.example.userservice.util.exceptions.RealtimeDatabaseException;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException e) {
        return generateResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException() {
        return generateResponseEntity(HttpStatus.BAD_REQUEST, "Illegal request argument.");
    }

    @ExceptionHandler(AuthenticationExecutionException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationExecutionException(AuthenticationExecutionException e) {
        return generateResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return generateResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<ExceptionResponse> handleFirebaseAuthException(FirebaseAuthException e) {
        return generateResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RealtimeDatabaseException.class)
    public ResponseEntity<ExceptionResponse> handleRealtimeDatabaseException(RealtimeDatabaseException e) {
        return generateResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ExceptionResponse> generateResponseEntity(HttpStatus httpStatus, String message) {
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), message);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
