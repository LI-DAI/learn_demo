package com.learn.admin.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.learn.admin.dao.MenuMapper;
import com.learn.admin.dao.RoleMapper;
import com.learn.admin.entity.Menu;
import com.learn.admin.entity.Role;
import com.learn.admin.entity.User;
import com.learn.admin.service.UserService;
import com.learn.common.utils.RedisUtil;
import com.learn.security.config.SecurityProperties;
import com.learn.security.entity.LoginUser;
import com.learn.security.service.PathSecurityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.learn.common.constant.Constant.getAuthoritiesCacheName;
import static java.util.stream.Collectors.toList;

/**
 * @author LD
 * @date 2021/5/15 21:30
 */
@Service
public class PathSecurityServiceImpl implements PathSecurityService {

    private final RoleMapper roleMapper;

    private final MenuMapper menuMapper;

    private final SecurityProperties properties;

    private final UserService userService;

    public PathSecurityServiceImpl(RoleMapper roleMapper, MenuMapper menuMapper, SecurityProperties properties, UserService userService) {
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.properties = properties;
        this.userService = userService;
    }

    @Override
    public Map<String, String> loadRequestMap() {
        List<Menu> menus = menuMapper.selectList(Wrappers.emptyWrapper());
        return menus.stream().filter(Objects::nonNull).filter(menu -> !menu.getUrl().equals("#"))
                .collect(Collectors.toMap(Menu::getUrl, Menu::getPerms));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>(10);
        String authoritiesCacheName = getAuthoritiesCacheName(username);
        if (RedisUtil.hasKey(authoritiesCacheName)) {
            //直接从缓存中取
            String authoritiesStr = (String) RedisUtil.get(authoritiesCacheName);
            if (StrUtil.isNotBlank(authoritiesStr)) {
                List<SimpleGrantedAuthority> grantedAuthorities = JSON.parseArray(authoritiesStr, SimpleGrantedAuthority.class);
                if (CollectionUtil.isNotEmpty(grantedAuthorities)) {
                    authorities.addAll(grantedAuthorities);
                }
            }
        } else {
            List<Role> roles = roleMapper.getRolesByUserId(user.getUserId());
            roles.stream().filter(Objects::nonNull).map(role -> "ROLE_" + role.getRoleKey())
                    .forEach(key -> authorities.addAll(AuthorityUtils.createAuthorityList(key)));

            List<Menu> menus = menuMapper.getMenuByUserId(user.getUserId());
            List<GrantedAuthority> perms = menus.stream().filter(Objects::nonNull).map(Menu::getPerms)
                    .distinct().map(SimpleGrantedAuthority::new).collect(toList());

            authorities.addAll(perms);

            RedisUtil.set(authoritiesCacheName, JSON.toJSONString(authorities), properties.getTokenValidityInHours(), TimeUnit.HOURS);
        }
        return new LoginUser(user.getUserId(), username, user.getPassword(), authorities);
    }


}
