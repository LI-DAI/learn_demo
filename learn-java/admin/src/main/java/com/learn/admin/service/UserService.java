package com.learn.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.admin.entity.User;
import org.springframework.web.multipart.MultipartFile;


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


    /**
     * 批量导入用户
     *
     * @param file 用户信息文件
     */
    void importUsers(MultipartFile file);

}
