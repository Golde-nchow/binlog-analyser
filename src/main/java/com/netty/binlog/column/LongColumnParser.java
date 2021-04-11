package com.netty.binlog.column;

import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description long 类型字段解析器
 * @date 2021/4/11 下午10:21
 */
public class LongColumnParser implements IColumnParser {

    /**
     * 解析值
     * @param byteBuf 缓冲区
     * @param metaLen 字段长度
     * @return 列对应的值
     */
    @Override
    public Object parser(ByteBuf byteBuf, Integer metaLen) {
        // 因为 long 是 32 位，所以是 4 字节
        return ByteUtil.readInt(byteBuf, 4);
    }
}
