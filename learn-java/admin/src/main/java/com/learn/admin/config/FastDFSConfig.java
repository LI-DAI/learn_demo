package com.learn.admin.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author LD
 * @date 2021/6/5 12:33
 */
@Configuration
@Import(FdfsClientConfig.class)
public class FastDFSConfig {
}
