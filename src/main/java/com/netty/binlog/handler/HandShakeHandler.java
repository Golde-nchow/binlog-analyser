package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author by chow
 * @Description 握手处理器
 * @date 2021/3/9 上午 0:55
 */
public class HandShakeHandler extends SimpleChannelInboundHandler<PackageData> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {

        System.out.println("======================== 握手数据包开始解析 ================================");

        // 获取解码后的内容
        ByteBuf content = msg.getContent();

        // 1字节：协议版本
        int protocolVersion = content.readByte();

        // 服务器版本：n字节，NullTerminatedString，以 00 结尾.
        String serverVersion = ByteUtil.readNullTerminatedString(content);

        // 线程ID：4字节，又称连接id
        int threadId = ByteUtil.readInt(content, 4);

        // auth-plugin-data 第1部分：8字节
        // 第一部分终止数据：1字节；内容固定为 0
        String authPluginDataPartOne = ByteUtil.readNullTerminatedString(content);

        // 服务器权能标志-1：2字节，低16位
        int capabilityFlagsLower = ByteUtil.readInt(content, 2);

        // 字符编码：1字节，只使用了低8位
        int characterSet = content.readByte();

        // 服务器状态标志位：2字节
        int serverStatus = ByteUtil.readInt(content, 2);

        // 服务器权能标志-2：2字节，高16位
        int capabilityFlagsUpper = ByteUtil.readInt(content, 2);

        // 1字节：若为客户端插件身份验证，则值为 auth-plugin-data-length；否则为0
        byte authPluginDataLen = content.readByte();

        // 保留位：10字节，都为0
        content.readBytes(10);

        // auth-plugin-data-part 第2部分
        String authPluginDataPartTwo = ByteUtil.readNullTerminatedString(content);

        System.out.println("协议版本：" + protocolVersion);
        System.out.println("服务器版本：" + serverVersion);
        System.out.println("连接ID：" + threadId);
        System.out.println("auth-plugin-data 第1部分：" + authPluginDataPartOne);
        System.out.println("服务器权能标志，低16位：" + capabilityFlagsLower);
        System.out.println("字符编码：" + characterSet);
        System.out.println("服务器状态标志位：" + serverStatus);
        System.out.println("服务器权能标志，高16位：" + capabilityFlagsUpper);
        System.out.println("auth-plugin-data-length：" + authPluginDataLen);
        System.out.println("auth-plugin-data 第2部分：" + authPluginDataPartTwo);

        System.out.println("======================== 握手数据包解析完成 ================================");
    }
}
