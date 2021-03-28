package com.netty.binlog.handler.fetcher;

import com.netty.binlog.constant.BinlogConstant;
import com.netty.binlog.constant.CommandTypes;
import com.netty.binlog.entity.dump.BinlogDumpRequest;
import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.entity.pack.PackageHeader;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author by chow
 * @Description 获取 serverId 处理器
 * @date 2021/3/28 下午1:00
 */
public class ServerIdFetcher extends AbstractFetcher {

    /**
     * 获取列的值
     *
     * @param content     netty 缓冲区
     * @param columnCount 列数量
     * @param fields      列数据
     */
    @Override
    public void getColumnValue(ByteBuf content, int columnCount, Map<String, String> fields) {
        super.getColumnValue(content, columnCount, fields);

        // 获取 serverId
        String serverId = "";
        Set<Map.Entry<String, String>> entries = fields.entrySet();
        for (Map.Entry<String, String> data : entries) {
            serverId = data.getValue();
        }

        if (StringUtils.isEmpty(BinlogConstant.SERVER_ID)) {
            BinlogConstant.SERVER_ID = serverId;
        }
    }

    /**
     * 获取列值的 EOF 数据报
     * @param content netty 缓冲区
     */
    @Override
    public void getColumnValueEof(ByteBuf content) {
        super.getColumnValueEof(content);

        // 发送 BINLOG_DUMP 数据报
        ByteBuf binlogDumpBuf = BinlogDumpRequest
                .builder()
                .status(CommandTypes.BINLOG_DUMP.ordinal())
                .binlogPos(Integer.parseInt(BinlogConstant.BIN_LOG_FILE_POSITION))
                .flags(1)
                .serverId(Integer.parseInt(BinlogConstant.SERVER_ID))
                .binlogFileName(BinlogConstant.BIN_LOG_FILE_NAME)
                .build()
                .toByteBuf();

        PackageHeader packageHeader = PackageHeader
                .builder()
                .payloadLength(binlogDumpBuf.readableBytes())
                .sequenceId(0)
                .build();

        PackageData data = PackageData
                .builder()
                .header(packageHeader)
                .content(binlogDumpBuf)
                .build();

        getCtx().writeAndFlush(data);
    }
}
