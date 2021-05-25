package com.learn.security.utils;

import com.learn.security.entity.LoginUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * @author LD
 * @date 2021/5/16 10:28
 */
public class SecurityUtil {

    public static LoginUser loginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsername() {
        return loginUser().getUsername();
    }

    public static Integer getUserId() {
        return loginUser().getUserId();
    }

    @SuppressWarnings("unchecked")
    public static List<GrantedAuthority> getAuthorities() {
        return (List<GrantedAuthority>) loginUser().getAuthorities();
    }
}
