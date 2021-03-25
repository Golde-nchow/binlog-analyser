package com.netty.binlog.handler;

import com.netty.binlog.constant.PackageHeaderConstant;
import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.entity.pack.PackageHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author by chow
 * @Description binlog解码器
 *
 * 目前暂时用于去除【数据包】头部信息
 * package 头部信息总共 4 字节。
 * 有效载荷长度 payload_length 3 字节；
 * 序列号 sequence_id 1 字节；
 * 有效载荷内容长度 = payload_length；
 *
 * 由于从网络接收到的都是字节，所以需要从字节转 POJO 对象；
 * 所以继承的是 ByteToMessageDecoder
 *
 * @date 2021/3/6 下午3:37
 */
public class BinlogDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in == null || in.readableBytes() < PackageHeaderConstant.HEADER_LENGTH) {
            return;
        }

        // 缓存读指针
        in.markReaderIndex();

        // 有效载荷长度
        int packagePayloadLength = ByteUtil.readInt(in, PackageHeaderConstant.PAYLOAD_BYTE_LENGTH);
        // 序列号
        int packageSequenceId = in.readByte();

        // 若可读长度小于有效载荷长度，那么不处理；
        // 并重置读指针，因为 ByteBuf 是可以随机读写的.
        if (in.readableBytes() < packagePayloadLength) {
            in.resetReaderIndex();
            return;
        }

        System.out.println(
                String.format(
                        "payloadLength = %s, sequenceId = %s",
                        packagePayloadLength,
                        packageSequenceId)
        );

        // 封装头部信息
        PackageHeader header = PackageHeader
                .builder()
                .payloadLength(packagePayloadLength)
                .sequenceId(packageSequenceId)
                .build();

        // 封装数据包信息
        ByteBuf content = Unpooled.copiedBuffer(in.readBytes(packagePayloadLength));
        PackageData data = PackageData
                .builder()
                .header(header)
                .content(content)
                .build();

        out.add(data);
    }
}
