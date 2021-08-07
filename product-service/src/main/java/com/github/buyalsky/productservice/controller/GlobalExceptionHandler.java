package com.github.buyalsky.productservice.controller;

import com.github.buyalsky.productservice.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int BAD_REQUEST_STATUS_CODE = BAD_REQUEST.value();
    private static final int NOT_FOUND_STATUS_CODE = NOT_FOUND.value();


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response = new ErrorResponse(BAD_REQUEST_STATUS_CODE, e.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ProductNotFoundException e) {
        ErrorResponse response = new ErrorResponse(NOT_FOUND_STATUS_CODE, e.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }


    private static class ErrorResponse {
        private final int statusCode;
        private final String message;
        private final long timeStamp;

        public ErrorResponse(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }

        public long getTimeStamp() {
            return timeStamp;
        }
    }
}
