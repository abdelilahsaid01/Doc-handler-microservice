package com.example.documentservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // Log the exception (if needed)
        // ex.printStackTrace();

        // Customize the error response
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        logException(ex);

        ErrorMessage errorMessage = new ErrorMessage("File not found", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    private void logException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
    }

    @Getter
    private static class ErrorMessage {
        private final String message;
        private final int statusCode;

        public ErrorMessage(String message, int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
        }

    }
}

