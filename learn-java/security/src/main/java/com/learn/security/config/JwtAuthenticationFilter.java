package com.learn.security.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.learn.common.entity.Result;
import com.learn.common.entity.ResultCode;
import com.learn.security.service.PathSecurityService;
import com.learn.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.learn.common.constant.Constant.AUTHENTICATION;
import static com.learn.common.utils.CommonUtil.print;

/**
 * @author LD
 * @date 2020/3/7 14:46
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties properties;
    private final PathSecurityService securityService;

    public JwtAuthenticationFilter(PathSecurityService securityService, SecurityProperties properties) {
        this.securityService = securityService;
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken(request);
        if (StrUtil.isNotBlank(token)) {
            Map<String, Object> tokenMap;
            try {
                tokenMap = JwtTokenUtil.parseJwtToken(token);
            } catch (Exception e) {
                log.error("Parsing Token Exception：{}", e.getMessage());
                print(Result.fail(ResultCode.EXPIRE_TOKEN), response);
                return;
            }

            String username = MapUtil.getStr(tokenMap, "username");

            //从user中获取权限数据，没在token中存储权限，防止jwt过长
            UserDetails userDetails = securityService.loadUserByUsername(username);
            List<SimpleGrantedAuthority> authorities = Convert.toList(SimpleGrantedAuthority.class, userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(upToken);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHENTICATION);
        if (StrUtil.isNotBlank(token) && token.startsWith(properties.getOnlineKey())) {
            return token.substring(properties.getOnlineKey().length()).trim();
        }
        return null;
    }

}
