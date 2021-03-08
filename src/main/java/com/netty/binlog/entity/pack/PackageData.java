package com.netty.binlog.entity.pack;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;

/**
 * @author by chow
 * @Description 数据包所有数据
 * @date 2021/3/8 下午10:15
 */
@Data
@Builder
public class PackageData {

    /**
     * 数据包头部信息
     */
    private PackageHeader header;

    /**
     * 数据包内容
     */
    private ByteBuf content;
}
