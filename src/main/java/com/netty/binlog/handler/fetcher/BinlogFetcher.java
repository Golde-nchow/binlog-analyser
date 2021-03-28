package com.netty.binlog.handler.fetcher;

import com.netty.binlog.constant.BinlogConstant;
import com.netty.binlog.entity.command.TextCommand;
import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.entity.pack.PackageHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import java.util.Map;

/**
 * @author by chow
 * @Description 获取 binlog 信息的处理器
 *
 * 问题：由于查询后的数据，是通过多个数据报进行响应的，所以如何一下子处理这些数据报？
 *
 * @date 2021/3/24 下午10:54
 */
@Setter
public class BinlogFetcher extends AbstractFetcher {

    /**
     * 获取 serverId
     *
     * 官方文档：https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_com_binlog_dump.html
     *
     * @param ctx 管道处理器上下文
     */
    private void fetchServerId(ChannelHandlerContext ctx) {

        ByteBuf textCommandBuf = new TextCommand("select @@server_id").toByteBuf();

        PackageHeader packageHeader = PackageHeader
                .builder()
                .payloadLength(textCommandBuf.readableBytes())
                .sequenceId(0)
                .build();

        PackageData data = PackageData
                .builder()
                .header(packageHeader)
                .content(textCommandBuf)
                .build();

        ctx.writeAndFlush(data);
    }

    /**
     * 重写获取列值逻辑
     * @param content netty 缓冲区
     * @param columnCount 列数量
     * @param fields 列数据
     */
    @Override
    public void getColumnValue(ByteBuf content, int columnCount, Map<String, String> fields) {
        int index = 1;
        while (index < columnCount) {
            String fieldValue = ByteUtil.readLenencString(content);

            System.out.println("第"+ (index) + "个字段的值：" + fieldValue);

            // TODO：这里需要优化下，因为贪图方便，属性值的位置写死，应该转换为 Map 进行一一对应
            if (index == 1) {
                BinlogConstant.BIN_LOG_FILE_NAME = fieldValue;

            } else if (index == 2) {
                BinlogConstant.BIN_LOG_FILE_POSITION = fieldValue;
            }
            index++;
        }
    }

    /**
     * 获取列值的 EOF 数据报
     *
     * @param content netty 缓冲区
     */
    @Override
    public void getColumnValueEof(ByteBuf content) {
        super.getColumnValueEof(content);

        // 若完成返回结果的解析，那么就发起 BINLOG_DUMP 请求，开始监听
        // 但是由于还缺少 server_id 参数，所以就发起获取 server_id 请求
         fetchServerId(getCtx());
    }
}
