package com.netty.binlog.entity.pack;

import lombok.Builder;
import lombok.Data;

/**
 * @author by chow
 * @Description 数据包头部信息
 * @date 2021/3/8 下午9:16
 */
@Data
@Builder
public class PackageHeader {

    /**
     * 有效载荷长度
     */
    private Integer payloadLength;

    /**
     * 序列号
     */
    private Integer sequenceId;
}
