package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author by chow
 * @Description 身份认证结果处理器
 *
 * 1、校验认证结果。
 *    1、若是 ERR_PACKAGE，那么数据包的第一个字节一定是 0xFF（ (byte)255 => (补码)1111 1111 => (原码)1000 0001 => -1 ）
 *       数据的存储是以补码的形式存在的，正数的补码是自己，负数的补码是：保留符号位，原码取反 + 1;
 *       https://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html
 *
 *    2、若是 OK_PACKAGE ，那么数据包的第一个字节一定是 0x00 或者 0xFE
 *       https://dev.mysql.com/doc/internals/en/packet-OK_Packet.html
 *
 * 2、认证完成后，发送查询 show master status，来获取: binlog 文件名称、文件内容位置
 *
 * @date 2021/3/21 下午8:38
 */
public class AuthenticationResultHandler extends SimpleChannelInboundHandler<PackageData> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {
        // 因为发送了 auth 数据包后，会再一次地被 BinlogDecoder 解析，所以只返回内容给下一个处理器
        ByteBuf contentBuf = msg.getContent();

        // 1字节：header
        byte resultDataHeader = contentBuf.readByte();
        boolean authSuccess = resultDataHeader == 0x0;
        if (authSuccess) {
            System.out.println("身份认证成功！");

        } else {

            // 2字节：error_code
            int errorCode = ByteUtil.readInt(contentBuf, 2);

            // 1字节：marker of the SQL State
            String sqlStateMarker = ByteUtil.readString(contentBuf, 1);

            // 5字节：sql state
            String sqlState = ByteUtil.readString(contentBuf, 5);

            // EOF（数据包剩余的部分）：错误信息
            String errorMsg = ByteUtil.readEofString(contentBuf);

            System.out.println("身份认证失败");
            System.out.println("错误码：" + errorCode);
            System.out.println("sql状态标记：" + sqlStateMarker);
            System.out.println("sql状态：" + sqlState);
            System.out.println("错误信息：" + errorMsg);
        }

    }
}
