package com.omnify.gayatri_blog.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleAllExceptions(Exception ex) {
            ex.printStackTrace(); // Log to console
            return ResponseEntity.status(500).body("Internal Server Error: " + ex.getMessage());
        }
}
