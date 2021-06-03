package com.learn.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.learn.security.anon.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LD
 * @date 2021/6/3 10:59
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public TaskController(ThreadPoolTaskScheduler threadPoolTaskScheduler, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @GetMapping("/test1")
    @AnonymousAccess
    public void test() {
        threadPoolTaskScheduler.schedule(
                () -> log.debug("Execute every five minutes..." + DateUtil.now()), new CronTrigger("0/5 * * * * ? "));
    }

    @GetMapping("/test2")
    @AnonymousAccess
    public void test2() {
        threadPoolTaskScheduler.schedule(
                () -> log.debug("Execute in one minute..." + DateUtil.now()), DateUtil.offsetMinute(DateUtil.date(), 1));
    }

    @GetMapping("/test3")
    @AnonymousAccess
    public void test3() {
        threadPoolTaskScheduler.execute(() -> log.debug("Execute immediately..." + DateUtil.now()));
    }
}
