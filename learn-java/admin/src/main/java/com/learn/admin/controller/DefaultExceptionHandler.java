package com.learn.admin.controller;

import com.learn.common.entity.Result;
import com.learn.common.entity.ResultCode;
import com.learn.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author LD
 * @date 2021/5/15 18:43
 *
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    /**
     * 未知异常处理
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<String> runtimeException(Throwable e) {
        log.error(getStackTrace(e));
        return Result.fail(e.getMessage());
    }

    /**
     * 无权限异常处理
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<String> accessDeniedException(AccessDeniedException e) {
        log.error(getStackTrace(e));
        return Result.fail(ResultCode.UN_AUTHENTICATION);
    }

    /**
     * 密码错误异常处理
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public Result<String> badCredentialsException(BadCredentialsException e) {
        log.error(getStackTrace(e));
        return Result.fail(ResultCode.BAD_CREDENTIALS);
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(getStackTrace(e));
        return Result.fail(ResultCode.FAILURE, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = BadRequestException.class)
    public Result<String> badRequestException(BadRequestException e) {
        log.error(getStackTrace(e));
        return Result.fail(ResultCode.FAILURE, e.getMessage());
    }

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
