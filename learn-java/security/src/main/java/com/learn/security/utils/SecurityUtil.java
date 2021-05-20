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

    public LoginUser loginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUsername() {
        return loginUser().getUsername();
    }

    public Integer getUserId() {
        return loginUser().getUserId();
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthorities() {
        return (List<GrantedAuthority>) loginUser().getAuthorities();
    }
}
