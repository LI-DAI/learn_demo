package com.learn.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LD
 * @date 2021/4/18 16:34
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
    private final static List<CallBack> CALL_BACKS = new ArrayList<>();
    private static boolean addCallback = true;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;

        if (SpringContextUtil.addCallback) {
            for (CallBack callBack : SpringContextUtil.CALL_BACKS) {
                callBack.executor();
            }
            SpringContextUtil.CALL_BACKS.clear();
        }

        SpringContextUtil.addCallback = false;
        log.info("callback is executed...");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    /**
     * 获取springboot配置文件中获取配置信息
     *
     * @param property     配置名
     * @param defaultValue 默认值
     * @param requiredType 返回值类型Class
     * @param <T>          返回值类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 发布事件
     *
     * @param applicationEvent 事件
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }

    /**
     * 添加回调事件, 对于某些需要 applicationContext 加载的资源等待 applicationContext 加载完成后自动执行
     *
     * @param callback 回调函数
     */
    public synchronized static void addCallBack(CallBack callback) {
        if (addCallback) {
            SpringContextUtil.CALL_BACKS.add(callback);
        } else {
            log.info("callback {} 已无法添加, 立即执行回调任务..", callback.getCallBackName());
            callback.executor();
        }
    }
}
