package com.learn.admin.controller;

import com.learn.admin.entity.UserRole;
import com.learn.common.entity.Result;
import com.learn.common.utils.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LD
 * @date 2021/5/16 9:17
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/anon")
    public Result<String> anon() {
        UserRole userRole = new UserRole(1, 2, 3);
        RedisUtil.set("test", userRole);
        UserRole test = (UserRole) RedisUtil.get("test");
        System.out.println(test);
        return Result.data("this is a test");
    }


}
