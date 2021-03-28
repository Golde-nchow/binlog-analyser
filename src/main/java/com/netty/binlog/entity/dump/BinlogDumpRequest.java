package com.netty.binlog.entity.dump;

import com.netty.binlog.constant.BinlogConstant;
import com.netty.binlog.constant.CommandTypes;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Builder;
import lombok.Data;

/**
 * @author by chow
 * @Description binlogDump 的请求数据
 * @date 2021/3/28 下午6:18
 */
@Data
@Builder
public class BinlogDumpRequest {

    /**
     * 备份协议其中一个类型：[0x12] COM_BINLOG_DUMP
     */
    private Integer status;

    /**
     * binlog 文件的写入位置
     */
    private Integer binlogPos;

    /**
     * 标志位：目前只有这个
     * 0x01：BINLOG_DUMP_NON_BLOCK：如果没有更多的事件发送一个EOF包而不是阻塞连接
     */
    private Integer flags;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * binlog 文件名
     */
    private String binlogFileName;

    public ByteBuf toByteBuf() {
        ByteBuf buffer = Unpooled.buffer();

        // status
        byte[] status = ByteUtil.writeInt(this.status, 1);
        buffer.writeBytes(status);
        // position
        byte[] position = ByteUtil.writeInt(this.binlogPos, 4);
        buffer.writeBytes(position);
        // flags
        byte[] flags = ByteUtil.writeInt(this.flags, 2);
        buffer.writeBytes(flags);
        // serverId
        byte[] serverId = ByteUtil.writeInt(this.serverId, 4);
        buffer.writeBytes(serverId);
        // binlog-fileName
        buffer.writeBytes(this.binlogFileName.getBytes());

        return buffer;
    }
}
