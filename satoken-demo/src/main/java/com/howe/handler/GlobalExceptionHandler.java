package com.howe.handler;

import com.howe.dto.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截 
    @ExceptionHandler
    public Result handlerException(Exception e) {
        e.printStackTrace(); 
        return Result.fail(e.getMessage());
    }
}