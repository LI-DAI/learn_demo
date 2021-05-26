package com.learn.admin.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableMap;
import com.learn.admin.entity.User;
import com.learn.admin.utils.RedisUtil;
import com.learn.common.entity.Result;
import com.learn.security.config.SecurityProperties;
import com.learn.security.entity.LoginUser;
import com.learn.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.learn.common.constant.Constant.*;
import static com.learn.common.entity.ResultCode.EXPIRE_TOKEN;

/**
 * @author LD
 * @date 2021/5/15 20:20
 */
@Slf4j
@RestController
@RequestMapping("/learn")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;

    public LoginController(AuthenticationManager authenticationManager, SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(upToken);
        LoginUser authUser = (LoginUser) authentication.getPrincipal();
        String accessToken = securityProperties.getOnlineKey() + JwtTokenUtil.createAccessToken(username);
        String refreshToken = JwtTokenUtil.createRefreshToken(REFRESH_PREFIX + username);
        Map<String, Object> authInfo = ImmutableMap.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken, "user", authUser);
        return Result.data(authInfo, "登陆成功");
    }

    @PostMapping("/logout")
    public Result<Object> logout(HttpServletRequest request) {
        String token = request.getHeader(AUTHENTICATION).substring(securityProperties.getOnlineKey().length()).trim();
        Map<String, Object> tokenMap = JwtTokenUtil.parseJwtToken(token);
        String username = MapUtil.getStr(tokenMap, "username");
        RedisUtil.delete(getAuthoritiesCacheName(username));
        return Result.data(null, "注销成功");
    }

    @PostMapping("/refresh")
    public Result<Object> refreshToken(String token) {
        if (JwtTokenUtil.isExpiration(token)) {
            return Result.fail(EXPIRE_TOKEN);
        }
        Map<String, Object> tokenMap = JwtTokenUtil.parseJwtToken(token);
        String refreshName = MapUtil.getStr(tokenMap, "username");
        if (StrUtil.isBlank(refreshName) || !refreshName.startsWith(REFRESH_PREFIX)) {
            return Result.fail(EXPIRE_TOKEN);
        }
        String username = refreshName.replace(REFRESH_PREFIX, "");
        String accessToken = securityProperties.getOnlineKey() + JwtTokenUtil.createAccessToken(username);
        String refreshToken = JwtTokenUtil.createRefreshToken(refreshName);
        Map<String, String> map = ImmutableMap.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
        return Result.data(map);
    }
}
