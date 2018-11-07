package com.hapicc.tasks.async;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class TestAsyncTask {

    @Async
    public Future<String> doTask1() throws Exception {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        System.out.println("任务 1 耗时：" + (end - start) + " ms");
        return new AsyncResult<>("666");
    }

    @Async
    public Future<String> doTask2() throws Exception {
        long start = System.currentTimeMillis();
        Thread.sleep(800);
        long end = System.currentTimeMillis();
        System.out.println("任务 2 耗时：" + (end - start) + " ms");
        return new AsyncResult<>("666");
    }

    @Async
    public Future<String> doTask3() throws Exception {
        long start = System.currentTimeMillis();
        Thread.sleep(600);
        long end = System.currentTimeMillis();
        System.out.println("任务 3 耗时：" + (end - start) + " ms");
        return new AsyncResult<>("666");
    }
}
