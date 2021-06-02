package com.learn.admin.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.admin.dao.UserMapper;
import com.learn.admin.entity.User;
import com.learn.admin.entity.UserRole;
import com.learn.admin.enums.Enabled;
import com.learn.admin.service.UserRoleService;
import com.learn.admin.service.UserService;
import com.learn.common.exception.BadRequestException;
import com.learn.common.utils.EasyExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private final UserRoleService userRoleService;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRoleService userRoleService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
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

    @Override
    public void importUsers(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery().select(User::getUsername).eq(User::getEnabled, Enabled.ENABLED.getValue());
        List<String> names = listObjs(wrapper, obj -> (String) obj);
        final AtomicInteger count = new AtomicInteger();
        EasyExcel.read(inputStream, User.class, EasyExcelUtil.getListener((List<User> users) -> {
            //如果username已存在则直接移除
            users.removeIf(user -> names.contains(user.getUsername()));
            for (User user : users) {
                user.setPassword(passwordEncoder.encode("hello"));
            }
            saveBatch(users);
            //为所有用户创建默认角色 ，默认为 COMMON
            List<UserRole> userRoles = users.stream().map(User::getUserId)
                    .map(userId -> new UserRole(null, userId, 2)).collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
            log.info("正在执行批量导入操作，导入数量：{}", count.addAndGet(users.size()));
        })).sheet().doReadSync();
        log.info("批量导入操作完成，导入总数：{}", count.get());
        IoUtil.close(inputStream);
    }

    public IPage<User> queryPages(Integer pageNum, Integer pageSize, String username, String nickname) {
        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .and(StrUtil.isNotBlank(username), query -> query.like(User::getUsername, username))
                .and(StrUtil.isNotBlank(nickname), query -> query.like(User::getNickname, nickname));
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }
}
