package com.learn.admin.controller;

import com.learn.admin.service.UserService;
import com.learn.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public Result<String> importUsers(@RequestParam("file") MultipartFile file) {
        userService.importUsers(file);
        return Result.data(null);
    }
}
