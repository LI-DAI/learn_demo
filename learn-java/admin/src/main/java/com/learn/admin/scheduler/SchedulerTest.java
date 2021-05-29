package com.learn.admin.scheduler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author LD
 * @date 2021/5/29 13:32
 */
public class SchedulerTest {

    public static void main(String[] args) {
        timerScheduler();

    }

    /**
     * 阻塞运行，延迟3秒执行
     */
    public static void timerScheduler() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("儿童节快乐");
            }
        }, 3000);
    }
}
