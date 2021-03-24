package com.netty.binlog.entity.command;

import com.netty.binlog.constant.CommandTypes;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author by chow
 * @Description 文本协议命令
 * @date 2021/3/23 下午10:23
 */
public class TextCommand {

    /**
     * 指定语句
     */
    private String command;

    /**
     * 构造方法
     * 传入 sql 语句，该语句只用于查询
     *
     * @param command sql 查询语句.
     */
    public TextCommand(String command) {
        this.command = command;
    }

    /**
     * 通过所有属性，生成 byteBuf 缓冲区数据
     * @return 缓冲区数据
     */
    public ByteBuf toByteBuf() {

        // 查询时，发送的数据报数据
        ByteBuf queryPackageData = Unpooled.buffer();

        // 1字节：command type
        // ordinal 的作用，若该枚举类是第一个，那么它的序列就是 0，返回的是 0.
        byte[] commandTypeBytes = ByteUtil.writeInt(CommandTypes.QUERY.ordinal(), 1);

        queryPackageData.writeBytes(commandTypeBytes);

        // string<EOF>：sql 语句
        queryPackageData.writeBytes(command.getBytes());

        return queryPackageData;
    }
}
