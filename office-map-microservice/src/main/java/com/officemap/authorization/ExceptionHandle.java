package com.officemap.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(UnauthorizedTokenException.class)
    public ResponseEntity<Void> unauthorizedTokenException(UnauthorizedTokenException ex, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
