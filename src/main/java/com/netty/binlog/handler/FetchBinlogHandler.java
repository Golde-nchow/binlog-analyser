package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author by chow
 * @Description 获取 binlog 信息的处理器
 *
 * 问题：由于查询后的数据，是通过多个数据报进行响应的，所以如何一下子处理这些数据报？
 *
 * @date 2021/3/24 下午10:54
 */
public class FetchBinlogHandler extends SimpleChannelInboundHandler<PackageData> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {

        System.out.println("======================== 开始解析 binlog 响应信息数据报 ========================");

        ByteBuf content = msg.getContent();
        int result = ByteUtil.readLenencInt(content);

        System.out.println("列数量：" + result);

    }
}
