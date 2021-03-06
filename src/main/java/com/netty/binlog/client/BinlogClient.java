package com.netty.binlog.client;

import com.netty.binlog.thread.ClientThread;
import com.netty.binlog.thread.ClientThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author by chow
 * @Description 客户端，监听 binlog 事件
 * @date 2021/3/6 下午2:17
 */
public class BinlogClient {

    /**
     * 自定义的客户端线程池
     */
    private static final ThreadPoolExecutor POOL_EXECUTOR = new ClientThreadPool();

    public static void main(String[] args) {
        POOL_EXECUTOR.execute(new ClientThread());
    }

}
