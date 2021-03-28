package com.netty.binlog.handler.fetcher;

import com.netty.binlog.constant.BinlogConstant;
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
}
