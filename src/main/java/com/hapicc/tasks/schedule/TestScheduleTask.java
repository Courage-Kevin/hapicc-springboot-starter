package com.hapicc.tasks.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduleTask {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    // 定义每隔 3 秒执行任务
//  @Scheduled(fixedRate = 3000)
    // 通过 Cron 表达式定义每隔 3 分钟执行任务
    // 在线 Cron 表达式生成：http://www.pppet.net 或 http://cron.qqe2.com
    @Scheduled(cron = "0 */3 * * * *")
    public void reportCurrentTime() {
        System.out.println("当前时间：" + sdf.format(new Date()));
    }
}
