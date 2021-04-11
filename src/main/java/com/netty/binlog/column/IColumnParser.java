package com.netty.binlog.column;

import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 字段解析器
 * @date 2021/4/11 下午9:51
 */
public interface IColumnParser {

    /**
     * 解析值
     * @param byteBuf 缓冲区
     * @param metaLen 字段长度
     * @return 列对应的值
     */
    Object parser(ByteBuf byteBuf, Integer metaLen);

}
