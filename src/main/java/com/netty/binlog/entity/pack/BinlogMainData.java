package com.netty.binlog.entity.pack;

import lombok.Builder;
import lombok.Data;

/**
 * @author by chow
 * @Description binlog 主要的数据：文件名、文件最后的写位置
 * @date 2021/3/28 上午11:10
 */
@Data
@Builder
public class BinlogMainData {

    /**
     * 文件名
     */
    private String binlogFileName;

    /**
     * position
     */
    private String position;

    /**
     * 服务器id
     */
    private String serverId;
}
