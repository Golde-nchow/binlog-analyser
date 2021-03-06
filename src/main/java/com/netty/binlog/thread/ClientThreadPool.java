package com.netty.binlog.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author by chow
 * @Description 客户端线程池
 * @date 2021/3/6 下午6:14
 */
public class ClientThreadPool extends ThreadPoolExecutor {

    public ClientThreadPool(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ClientThreadPool() {
        super(8,
                16,
                300,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ClientThreadFactory()
        );
    }
}
