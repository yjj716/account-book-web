package com.yjj.accountbook.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e){
        String msg=e.getBindingResult().getFieldErrors().stream().findFirst().map(err->
                err.getDefaultMessage()).orElse("参数不合法");
        return Result.fail(400,msg);
    }
    @ExceptionHandler(Exception.class)
    public Result<Void>handleOther(Exception e){
        log.error("未预期异常", e);
        return Result.fail(500,"服务器开小差了，请稍后再试");
    }
}
