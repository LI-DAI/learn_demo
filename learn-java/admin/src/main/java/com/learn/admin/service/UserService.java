package com.learn.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.admin.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @author LD
 * @date 2020/3/5 16:57
 */
public interface UserService extends IService<User> {

    /**
     * 获取User
     *
     * @param username 用户名
     * @return 用户
     */
    User findUserByUsername(String username);

    /**
     * 注册
     *
     * @param user 用户信息
     * @return true：注册成功
     */
    boolean register(User user);

    /**
     * 修改用户信息
     *
     * @param user 信息
     * @return /
     */
    User updateUser(User user);


    /**
     * 批量导入用户
     *
     * @param file 用户信息文件
     * @throws IOException /
     */
    void importUsers(MultipartFile file) throws IOException;

    /**
     * 分页获取用户
     *
     * @param pageNum  页码
     * @param pageSize 页数
     * @param username 用户名
     * @param nickname 昵称
     * @return /
     */
    IPage<User> queryPages(Integer pageNum, Integer pageSize, String username, String nickname);

}
