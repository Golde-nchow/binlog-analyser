package com.netty.binlog.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by chow
 * @Description 客户端线程工厂
 * @date 2021/3/6 下午6:07
 */
public class ClientThreadFactory implements ThreadFactory {

    /**
     * 线程编号
     */
    private AtomicInteger index = new AtomicInteger(1);

    private final ThreadGroup group;

    public ClientThreadFactory() {
        SecurityManager sm = System.getSecurityManager();
        group = (sm != null) ? sm.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, "binlog客户端线程-" + index.getAndIncrement());
    }
}
