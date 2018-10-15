package org.n3r.idworker;

import org.n3r.idworker.strategy.DefaultWorkerIdStrategy;
import org.n3r.idworker.utils.Utils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Sid {
    private static WorkerIdStrategy workerIdStrategy;
    private static IdWorker idWorker;

    static {
        configure(DefaultWorkerIdStrategy.instance);
    }


    public static synchronized void configure(WorkerIdStrategy custom) {
        if (workerIdStrategy != null) workerIdStrategy.release();
        workerIdStrategy = custom;
        idWorker = new IdWorker(workerIdStrategy.availableWorkerId()) {
            @Override
            public long getEpoch() {
                return Utils.midnightMillis();
            }
        };
    }

    /**
     * 一天最大毫秒 86400000，最大占用 27 比特
     * 27 + 10 + 11 = 48 位，最大值 281474976710655(15字)，YK0XXHZ827(10字)
     * 6 位(YYMMDD) + 15 位，共 21 位
     *
     * @return 固定 21 位数字字符串
     */
    public /*static*/ String next() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + String.format("%015d", id);
    }


    /**
     * 返回固定 16 位的字母数字混编的字符串
     * 
     * @return 固定 16 位的字母数字混编的字符串
     */
    public /*static*/ String nextShort() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + Utils.padLeft(Utils.encode(id), 10, '0');
    }
}
