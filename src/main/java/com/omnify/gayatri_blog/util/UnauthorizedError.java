package com.omnify.gayatri_blog.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedError extends RuntimeException  {
        public UnauthorizedError(String message) {
            super(message);
        }
}
