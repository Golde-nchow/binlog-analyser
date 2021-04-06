package com.netty.binlog.event;

import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author by chow
 * @Description 格式化描述事件，表明事件是如何布局的
 * @date 2021/3/29 下午10:48
 */
public class FormatDescriptionEventParser implements IEventParser {

    @Override
    public void parse(EventHeader eventHeader, ByteBuf content) {

        // binlog 版本
        int binlogVersion = ByteUtil.readInt(content, 2);

        // mysqlServer 版本
        String mysqlServerVersion = ByteUtil.readString(content, 50);

        // 创建时间
        int createTimestamp = eventHeader.getTimestamp();
        content.skipBytes(4);

        // 事件头部长度
        int eventHeaderLength = content.readByte();

        System.out.println("binlog 版本：" + binlogVersion);
        System.out.println("mysqlServer 版本：" + mysqlServerVersion);
        System.out.println("binlog 创建时间：" + LocalDateTime.ofEpochSecond(createTimestamp, 0, ZoneOffset.ofHours(8)).toString());
        System.out.println("binlog 事件头部长度：" + eventHeaderLength);

        System.out.println("==================== 格式化描述事件解析完成 ===================");
    }

}
