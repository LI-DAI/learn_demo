package com.learn.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LD
 * @date 2020/4/18 9:55
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.config")
public class SecurityProperties {

    private String header;

    private Integer tokenValidityInHours;

    private String onlineKey;

    private Set<String> anonUri = new HashSet<>(20);
}
