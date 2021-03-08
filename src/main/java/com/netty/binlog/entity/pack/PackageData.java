package com.netty.binlog.entity.pack;

import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;
import lombok.Data;

/**
 * @author by chow
 * @Description 数据包所有数据
 * @date 2021/3/8 下午10:15
 */
@Data
@Builder
public class PackageData {

    /**
     * 数据包头部信息
     */
    private PackageHeader header;

    /**
     * 数据包内容
     */
    private ByteBuf content;

    /**
     * 编码为 ByteBuf
     */
    public void encodeAsByteBuf(ByteBuf out) {
        Integer sequenceId = header.getSequenceId();

        // 头部信息4字节
        out.writeBytes(ByteUtil.writeInt(header.getPayloadLength(), 3));
        out.writeByte(sequenceId);
        // 剩下就是内容
        out.writeBytes(content);
    }
}
