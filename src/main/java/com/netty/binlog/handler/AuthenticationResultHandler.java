package com.netty.binlog.handler;

import com.netty.binlog.constant.ProtocolStatusFlags;
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

        System.out.println("======================== 开始解析身份认证结果 ================================");

        // 因为发送了 auth 数据包后，会再一次地被 BinlogDecoder 解析，所以只返回内容给下一个处理器
        // 目前只处理 CLIENT_PROTOCOL_41 协议
        ByteBuf contentBuf = msg.getContent();

        // 1字节：header
        byte resultDataHeader = contentBuf.readByte();
        boolean authSuccess = resultDataHeader == 0x0;

        // 开始处理认证返回结果
        if (authSuccess) {
            authSuccess(contentBuf);
        } else {
            authFail(contentBuf);
        }

        System.out.println("======================== 身份认证结果解析完毕 ================================");

    }

    /**
     * 认证成功逻辑
     * @param contentBuf 内容缓冲区
     */
    private void authSuccess(ByteBuf contentBuf) {
        // int<lenenc>（会根据内容动态地更改字节长度）
        // int<lenenc>：affected_rows
        int affectedRows = ByteUtil.readLenencInt(contentBuf);

        // int<lenenc>：last_insert_id
        int lastInsertId = ByteUtil.readLenencInt(contentBuf);

        // 2字节：status_flags：状态位
        int statusFlags = ByteUtil.readInt(contentBuf, 2);

        // 2字节：number of warnings：警告的数量
        int warningNumber = ByteUtil.readInt(contentBuf, 2);

        // StringEOF：readable status information：可读状态信息
        String statusInformation = ByteUtil.readEofString(contentBuf);

        System.out.println("身份认证成功！");
        System.out.println("影响的行数：" + affectedRows);
        System.out.println("最后插入的主键id：" + lastInsertId);
        System.out.println("状态位：" + ProtocolStatusFlags.getStatusFlagsDescription(statusFlags));
        System.out.println("警告的数量：" + warningNumber);
        System.out.println("可读状态信息：" + statusInformation);
    }

    /**
     * 身份认证失败逻辑
     * @param contentBuf 内容缓冲区
     */
    private void authFail(ByteBuf contentBuf) {
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
