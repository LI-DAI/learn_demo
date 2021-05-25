package com.learn.admin.service.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.admin.dao.UserMapper;
import com.learn.admin.entity.User;
import com.learn.admin.entity.UserRole;
import com.learn.admin.service.UserRoleService;
import com.learn.admin.service.UserService;
import com.learn.common.exception.BadRequestException;
import com.learn.common.utils.EasyExcelUtil;
import com.learn.security.utils.SecurityUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
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
    @SneakyThrows
    public void importUsers(MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        String loginName = SecurityUtil.getUsername();
        List<String> names = listObjs(Wrappers.<User>lambdaQuery().select(User::getUsername).eq(User::getEnabled, 1), obj -> (String) obj);
        EasyExcel.read(inputStream, User.class, EasyExcelUtil.getListener((List<User> users) -> {

            //如果username已存在则直接移除
            users.removeIf(user -> names.contains(user.getUsername()));

            for (User user : users) {
                //导入用户默认密码为：hello
                user.setPassword(passwordEncoder.encode("hello"));
            }
            saveBatch(users);

            List<UserRole> userRoles = users.stream().map(User::getUserId)
                    //这里2为 COMMON 角色的 id
                    .map(userId -> new UserRole(null, userId, 2)).collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);

            log.info("{} 执行批量导入用户操作，导入数量：{}", loginName, users.size());

        })).sheet().doRead();

        IoUtil.close(inputStream);

    }

}
