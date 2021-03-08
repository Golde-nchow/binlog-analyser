package com.netty.binlog.util;

import io.netty.buffer.ByteBuf;

/**
 * @author by chow
 * @Description 字节工具类
 * @date 2021/3/6 下午4:25
 */
public class ByteUtil {

    /**
     * 使用小端模式，读取 ByteBuf 指定长度的整数类型数据
     * @param src ByteBuf 数据
     * @param length 字节长度
     * @return 整数类型数据
     * @throws NullPointerException 源数据 src 为空，
     *                              或者读取长度 length 为 0
     */
    public static int readInt(ByteBuf src, int length) {
        if (length == 0) {
            throw new NullPointerException("源数据读取长度不能为空");
        }

        if (src == null) {
            throw new NullPointerException("源数据 ByteBuffer 不能为空");
        }

        /*
         * 小端转大端
         * 例如：
         * 读取3字节；那么高字节的数据保存在最左，低字节的数据保存在最右；
         * 小端低8位左移16位，那么就变为大端低8位的字节。
         * 最后一个字节是不需要移动的。
         */
        int result = 0;
        for (int i = 0; i < length; i++) {
            result |= (src.readUnsignedByte() << (i * 8));
        }

        return result;
    }

    /**
     * 将大端的数字，转为小端模式字节数据
     * @param data 大端模式数据
     * @param length 长度
     * @return 小端模式字节数据
     */
    public static byte[] writeInt(Integer data, int length) {

        if (data == null || length < 0) {
            return new byte[0];
        }

        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) ((data >> (8 * i)) & 0x000000FF);
        }

        return result;
    }
}
