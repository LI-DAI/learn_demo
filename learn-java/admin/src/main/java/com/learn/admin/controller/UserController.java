package com.learn.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.learn.admin.entity.User;
import com.learn.admin.service.UserService;
import com.learn.common.entity.Result;
import com.learn.security.anon.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author LD
 * @date 2021/5/24 17:52
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/import")
    public Result<String> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        userService.importUsers(file);
        return Result.data(null);
    }

    @GetMapping("/page")
    public Result<IPage<User>> queryPages(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(required = false, value = "username") String username,
                                          @RequestParam(required = false, value = "nickname") String nickname) {
        return Result.data(userService.queryPages(pageNum, pageSize, username, nickname));
    }

    @PostMapping("/register")
    @AnonymousAccess
    public Result<Object> register(@RequestBody @Validated User user) {
        return Result.data(userService.register(user));
    }

    @PutMapping("/update")
    public Result<Object> updateUser(@RequestBody @Validated User user) {
        return Result.data(userService.updateUser(user));
    }


}
