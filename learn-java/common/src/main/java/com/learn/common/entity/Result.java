package com.learn.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LD
 * @date 2020/3/5 10:56
 */
@Data
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    private Result(ResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private Result(ResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private Result(ResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private Result(ResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }


    public static <T> Result<T> data(T data) {
        return data(data, ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> data(T data, String msg) {
        return data(ResultCode.SUCCESS.getCode(), data, msg);
    }

    public static <T> Result<T> data(int code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAILURE);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultCode.FAILURE, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, null, msg);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result<>(resultCode, msg);
    }


}
