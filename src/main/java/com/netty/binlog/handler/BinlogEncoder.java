package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.PackageData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author by chow
 * @Description Message转 Byte编码类
 * @date 2021/3/8 下午10:25
 */
public class BinlogEncoder extends MessageToByteEncoder<PackageData> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PackageData msg, ByteBuf out) throws Exception {
        msg.encodeAsByteBuf(out);
    }
}
