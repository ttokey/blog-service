package com.ttokey.blog.advice;

import com.ttokey.blog.exception.TtokeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = TtokeyException.class)
    public String handleTtokeyException(TtokeyException ttokeyException) {
        return ttokeyException.getMessage();
    }
}
