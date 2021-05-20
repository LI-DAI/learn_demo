package com.learn.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LD
 * @date 2020/3/5 10:57
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILURE(500, "操作失败"),

    UN_AUTHENTICATION(-100, "未认证"),
    UN_AUTHORIZATION(-101, "未授权"),
    BAD_CREDENTIALS(-102, "用户名或密码错误"),
    LOCKED(-103, "账户已锁定"),
    EXPIRE_TOKEN(-104, "无效的Token");

    final int code;

    final String message;

}
