package com.omnify.gayatri_blog.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandling {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
            ex.printStackTrace(); // Log to console
            return ResponseEntity.status(500).body(Map.of("Error", ex.toString()));
        }

        @ExceptionHandler(UnauthorizedError.class)
        public ResponseEntity<Map<String, String>> UnauthorizedException(Exception ex) {
            ex.printStackTrace(); // Log to console
            return ResponseEntity.status(401).body(Map.of("Error", ex.toString()));
        }

        @ExceptionHandler(NotFoundError.class)
        public ResponseEntity<Map<String, String>> NotFoundException(Exception ex) {
            ex.printStackTrace(); // Log to console
            return ResponseEntity.status(404).body(Map.of("Error", ex.toString()));
        }

        @ExceptionHandler(BadRequestError.class)
        public ResponseEntity<Map<String, String>> BadRequestException(Exception ex) {
            ex.printStackTrace(); // Log to console
            return ResponseEntity.status(400).body(Map.of("Error", ex.toString()));
        }
}

