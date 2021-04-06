package com.netty.binlog.event;

import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 查询事件
 * @date 2021/4/1 下午11:07
 */
public class QueryEventParser implements IEventParser {

    /**
     * 执行解析
     * @param content 缓冲区
     */
    @Override
    public void parse(EventHeader eventHeader, ByteBuf content) {
        // 代理id
        int slaveProxyId = ByteUtil.readInt(content, 4);
        // sql 执行时间
        int executionTime = ByteUtil.readInt(content, 4);
        // 数据库长度
        int schemaLength = ByteUtil.readInt(content, 1);
        // 错误码
        int errorCode = ByteUtil.readInt(content, 2);
        // status_vars 长度
        int statusVarsLength = ByteUtil.readInt(content, 2);

        System.out.println("sql 执行时间：" + executionTime);
        System.out.println("数据库长度：" + schemaLength);
        System.out.println("错误码：" + errorCode);

        // 计算 sql 长度，官方文档：事件头部的事件长度 - 当前读位置
        int sqlLength = eventHeader.getEventSize() - content.readerIndex();
        System.out.println("数据库名称：" + ByteUtil.readString(content, schemaLength));
        System.out.println("执行的 sql：" + ByteUtil.readString(content, sqlLength));

        System.out.println("========================== 查询事件解析完成 ==========================");
    }
}
