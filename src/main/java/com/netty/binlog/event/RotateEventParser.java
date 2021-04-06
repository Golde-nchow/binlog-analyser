package com.netty.binlog.event;

import com.netty.binlog.constant.BinlogConstant;
import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author by chow
 * @Description rotate 事件：切换 binlog 时触发该事件
 * @date 2021/4/1 下午10:31
 */
@Data
public class RotateEventParser implements IEventParser {

    /**
     * 执行解析
     * @param content 缓冲区
     */
    @Override
    public void parse(EventHeader eventHeader, ByteBuf content) {
        // 切换的 binlog 位置
        int position = ByteUtil.readInt(content, 8);
        // 切换的 binlog 文件名
        String binlogName = ByteUtil.readEofString(content);

        // 设置到全局
        BinlogConstant.BIN_LOG_FILE_POSITION = String.valueOf(position);
        BinlogConstant.BIN_LOG_FILE_NAME = binlogName;

        System.out.println("切换 binlog 后的位置：" + position);
        System.out.println("切换到的 binlog：" + binlogName);

        System.out.println("=============== binlog 切换事件解析完成 ====================");
    }
}
