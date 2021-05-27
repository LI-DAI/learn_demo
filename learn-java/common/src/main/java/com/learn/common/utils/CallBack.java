package com.learn.common.utils;

/**
 * @author LD
 * @date 2021/5/16 16:32
 */
public interface CallBack {

    /**
     * 执行
     */
    void executor();

    /**
     * 回调任务名称
     *
     * @return /
     */
    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}
