package com.netty.binlog.event;

import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 事件处理器接口
 * @date 2021/3/29 下午11:27
 */
public interface IEventParser {

    /**
     * 执行解析
     * @param content 缓冲区
     */
    void parse(ByteBuf content);

}
