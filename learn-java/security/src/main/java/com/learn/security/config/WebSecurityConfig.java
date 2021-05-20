package com.learn.security.config;

import cn.hutool.core.convert.Convert;
import com.learn.common.entity.Result;
import com.learn.common.entity.ResultCode;
import com.learn.security.path.PathAccessDecisionManager;
import com.learn.security.path.PathFilterInvocationSecurityMetadataSource;
import com.learn.security.service.PathSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.learn.common.utils.CommonUtil.print;

/**
 * @author LD
 * @date 2021/5/15 18:47
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties properties;

    private final PathAccessDecisionManager pathAccessDecisionManager;

    private final PathFilterInvocationSecurityMetadataSource securityMetadataSource;

    private final PathSecurityService pathSecurityService;

    public WebSecurityConfig(SecurityProperties properties, PathAccessDecisionManager pathAccessDecisionManager, PathFilterInvocationSecurityMetadataSource securityMetadataSource, PathSecurityService pathSecurityService) {
        this.properties = properties;
        this.pathAccessDecisionManager = pathAccessDecisionManager;
        this.securityMetadataSource = securityMetadataSource;
        this.pathSecurityService = pathSecurityService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(pathSecurityService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JwtAuthenticationFilter(pathSecurityService, properties), UsernamePasswordAuthenticationFilter.class)
                //禁用csrf,
                .csrf().disable()
                //关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //支持跨域
                .cors()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .withObjectPostProcessor(objectPostProcessor())
                .and()
                .exceptionHandling()
                //未登录处理
                .authenticationEntryPoint((request, response, e) -> print(Result.fail(ResultCode.UN_AUTHENTICATION), response))
                //无权限处理
                .accessDeniedHandler((request, response, e) -> print(Result.fail(ResultCode.UN_AUTHORIZATION), response));
    }


    public ObjectPostProcessor<FilterSecurityInterceptor> objectPostProcessor() {
        return new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setSecurityMetadataSource(securityMetadataSource);
                object.setAccessDecisionManager(pathAccessDecisionManager);
                return object;
            }
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(anonymousAccess())
                .antMatchers("/websocket/**")
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    public String[] anonymousAccess() {
        return Convert.toStrArray(properties.getAnonUri());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
