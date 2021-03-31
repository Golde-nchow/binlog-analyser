package com.netty.binlog.entity.pack;

import lombok.*;

/**
 * @author by chow
 * @Description binlog 事件头部信息
 * @date 2021/3/30 上午12:22
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventHeader {

    /**
     * 创建 binlog 的时间
     */
    private Integer timestamp;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 服务器 id
     */
    private Integer serverId;

    /**
     * 事件长度：header + post-header + body)
     */
    private Integer eventSize;

    /**
     * binlog 的下个位置
     */
    private Integer nextLogPosition;

    /**
     * 事件头部标志位
     */
    private Integer flags;

}
