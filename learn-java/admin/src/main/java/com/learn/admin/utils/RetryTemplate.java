package com.learn.admin.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class RetryTemplate<T> {

    //重试次数
    private Integer retryTime = 2;

    //执行失败睡眠时间 ms
    private Long sleepMills = 500L;

    public RetryTemplate<T> setRetryTime(Integer retryTime) {
        if (retryTime < 1) {
            throw new IllegalArgumentException("retryTime 不合法");
        }
        this.retryTime = retryTime;
        return this;
    }

    public RetryTemplate<T> setSleepTime(Long sleepMills) {
        if (sleepMills < 0) {
            throw new IllegalArgumentException("sleepMills 不合法");
        }
        this.sleepMills = sleepMills;
        return this;
    }

    protected abstract T process() throws Exception;

    public T execute() {
        for (int i = 1; i <= retryTime; i++) {
            try {
                return process();
            } catch (Exception e) {
                log.error("重试业务异常，重试次数：{}，异常：{}", i, e.getMessage());
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMills);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    log.error("睡眠异常");
                    return null;
                }
            }
        }
        return null;
    }


}


class TestRetry {
    public static void main(String[] args) {
        Boolean bool = new RetryTemplate<Boolean>() {
            @Override
            protected Boolean process() throws Exception {
                System.out.println(1 / 0);
                return false;
            }
        }.setRetryTime(2).execute();

    }
}