package com.learn.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.admin.entity.User;


/**
 * @author LD
 * @date 2020/3/5 16:57
 */
public interface UserService extends IService<User> {

    /**
     * 获取User
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 注册
     *
     * @param user 用户信息
     * @return
     */
    boolean register(User user);

    /**
     * 修改用户信息
     *
     * @param user 信息
     */
    User updateUser(User user);

}
