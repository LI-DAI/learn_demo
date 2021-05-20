package com.learn.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * @author LD
 * @date 2021/5/15 21:26
 */
public interface PathSecurityService extends UserDetailsService {

    /**
     * 从db加载访问路径需要的权限map
     *
     * @return Map
     */
    Map<String, String> loadRequestMap();
}
