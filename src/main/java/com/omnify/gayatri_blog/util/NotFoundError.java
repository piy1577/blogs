package com.omnify.gayatri_blog.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundError extends RuntimeException {
    public NotFoundError(String message){
        super(message);
    }

}
