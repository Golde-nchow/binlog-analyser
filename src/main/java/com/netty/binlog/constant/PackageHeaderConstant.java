package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description binlog 数据包头部信息常量类
 * @date 2021/3/6 下午5:46
 */
public interface PackageHeaderConstant {

    /**
     * 数据包头部信息长度
     */
    Integer HEADER_LENGTH = 4;

    /**
     * 有效载荷长度（3字节）
     */
    Integer PAYLOAD_BYTE_LENGTH = 3;

    /**
     * 序列号长度（1字节）
     */
    Integer SEQUENCE_ID = 1;

}
