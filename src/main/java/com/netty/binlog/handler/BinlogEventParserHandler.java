package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.event.EventParserFactory;
import com.netty.binlog.event.IEventParser;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author by chow
 * @Description binlog 解析处理器
 *
 * 每次收到事件的响应数据包，解析都分为两步：
 * 1、获取 event 头部信息
 * 2、根据事件类型，使用不同的解析器，定位事件内容
 *
 * 每个事件都有一层头部信息
 *
 * @date 2021/3/29 下午10:25
 */
public class BinlogEventParserHandler extends SimpleChannelInboundHandler<PackageData> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {
        ByteBuf content = msg.getContent();
        if (content.readableBytes() < 20) {
            return;
        }

        // 获取 EventHeader
        int available = content.readByte();
        int timestamp = ByteUtil.readInt(content, 4);
        int eventType = ByteUtil.readInt(content, 1);
        int serverId = ByteUtil.readInt(content, 4);
        int eventSize = ByteUtil.readInt(content, 4);
        int nextLogPosition = ByteUtil.readInt(content, 4);
        int flags = ByteUtil.readInt(content, 2);

        EventHeader eventHeader = EventHeader
                .builder()
                .timestamp(timestamp)
                .eventType(eventType)
                .serverId(serverId)
                .eventSize(eventSize)
                .nextLogPosition(nextLogPosition)
                .flags(flags)
                .build();

        System.out.println("available：" + available);
        System.out.println(eventHeader);

        // 根据事件类型，获取解析器
        IEventParser eventParser = EventParserFactory.getEventParser(eventType);

        if (eventParser == null) {
            System.out.println("遇到不支持的事件类型");
        } else {
            eventParser.parse(eventHeader, content);
        }
    }
}
