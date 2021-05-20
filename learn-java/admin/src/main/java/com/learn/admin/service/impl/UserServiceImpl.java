package com.learn.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.admin.dao.UserMapper;
import com.learn.admin.entity.User;
import com.learn.admin.service.UserService;
import com.learn.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LD
 * @date 2020/3/5 16:58
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@CacheConfig(cacheNames = "userInfo")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserByUsername(String username) {
        Wrapper<User> wrapper = Wrappers.<User>query().lambda().eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public boolean register(User user) {
        Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (count > 0) {
            throw new BadRequestException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.save(user);
    }

    @Override
    public User updateUser(User user) {
        Integer count = userMapper.selectCount(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .ne(User::getUserId, user.getUserId()));
        if (count > 0) {
            throw new BadRequestException("用户名已存在");
        }
        userMapper.updateById(user);
        return user;
    }

}
