package com.netty.binlog.column;

import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author by chow
 * @Description VARCHAR 类型字段解析器
 * @date 2021/4/11 下午9:50
 */
@Data
public class VarcharColumnParser implements IColumnParser {

    /**
     * 解析值
     *
     * @param byteBuf 缓冲区
     * @param metaLen 字段长度
     * @return 列对应的值
     */
    @Override
    public Object parser(ByteBuf byteBuf, Integer metaLen) {
        int oneByteMax = 256;
        int byteOfLen = metaLen < oneByteMax ? 1 : 2;
        int columnValueLen = ByteUtil.readInt(byteBuf, byteOfLen);
        return ByteUtil.readString(byteBuf, columnValueLen);
    }
}
