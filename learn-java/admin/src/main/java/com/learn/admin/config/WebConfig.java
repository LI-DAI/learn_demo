package com.learn.admin.config;

import com.learn.common.utils.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LD
 * @date 2021/5/25 17:09
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
