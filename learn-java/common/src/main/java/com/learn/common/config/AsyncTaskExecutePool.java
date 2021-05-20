package com.learn.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author LD
 * @date 2021/4/23 15:10
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncTaskExecutePool implements AsyncConfigurer {

    private final AsyncTaskProperties properties;

    public AsyncTaskExecutePool(AsyncTaskProperties properties) {
        this.properties = properties;
    }

    @Override
    public Executor getAsyncExecutor() {
        log.info("Task thread pool initializing...");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        //线程前缀名
        executor.setThreadNamePrefix("bs-async-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("An exception occurred in this method -> " + method.getName());
            log.error(String.format("exception message : %s ; cause : %s", throwable.getMessage(), throwable.getCause()));
        };
    }
}
