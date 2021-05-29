package com.learn.admin.controller;

import com.learn.admin.entity.UserRole;
import com.learn.admin.utils.RedisUtil;
import com.learn.common.entity.Page;
import com.learn.common.entity.Result;
import com.learn.security.anon.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LD
 * @date 2021/5/16 9:17
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/anon")
    @AnonymousAccess
    public Result<Object> anon() {
        UserRole userRole = new UserRole(1, 2, 3);
        RedisUtil.set("test", userRole);
        UserRole test = (UserRole) RedisUtil.get("test");
        log.info("redis user role is {}", test);
        System.out.println(RedisUtil.scan("*"));
        Page<String> page = RedisUtil.pageScan("*", 3, 10);
        return Result.data(page);
    }


}
