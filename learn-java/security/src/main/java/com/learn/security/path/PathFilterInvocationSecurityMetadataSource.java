package com.learn.security.path;

import com.learn.security.service.PathSecurityService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Map;

/**
 * @author LD
 * @date 2021/5/15 18:59
 */
@Component
public class PathFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final PathSecurityService pathSecurityService;

    public PathFilterInvocationSecurityMetadataSource(PathSecurityService pathSecurityService) {
        this.pathSecurityService = pathSecurityService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        FilterInvocation invocation = (FilterInvocation) object;

        String requestUrl = invocation.getRequestUrl();

        Map<String, String> requestMap = pathSecurityService.loadRequestMap();

        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            if (pathMatcher.match(requestUrl, entry.getKey())) {
                return SecurityConfig.createList(entry.getValue());
            }
        }

        //没匹配到权限, 则返回需要登陆
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
