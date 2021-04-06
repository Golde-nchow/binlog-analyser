package com.netty.binlog.event;

import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.entity.pack.PackageHeader;
import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 事件处理器接口
 * @date 2021/3/29 下午11:27
 */
public interface IEventParser {

    /**
     * 执行解析
     * @param eventHeader 事件头部信息，某些字段需要通过包长度计算
     * @param content 缓冲区
     */
    void parse(EventHeader eventHeader, ByteBuf content);

}
