package com.netty.binlog.util;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * 读取指定字节的字符，并且拼接成字符串
     * @param contentBuf ByteBuf 缓冲区
     * @param length 长度
     */
    public static String readString(ByteBuf contentBuf, int length) {
        byte[] stringArr = new byte[length];
        contentBuf.readBytes(stringArr);
        return new String(stringArr, 0, length);
    }

    /**
     * 读取剩余字节的字符，并且拼接成字符串
     * @param contentBuf ByteBuf 缓冲区
     */
    public static String readEofString(ByteBuf contentBuf) {
        int readableBytes = contentBuf.readableBytes();
        byte[] readableBytesArr = new byte[readableBytes];
        contentBuf.readBytes(readableBytesArr);
        return new String(readableBytesArr, 0, readableBytesArr.length - 1);
    }

    /**
     * 获取 string<NUL> 类型的数据.
     * 此类数据没有长度限制，直至 00 结尾，不同数据包代表不同的数据.
     * @param buf ByteBuf 缓冲区
     */
    public static String readNullTerminatedString(ByteBuf buf) {
        if (buf == null) {
            return "";
        }

        int length = 0;
        // 暂存读指针
        buf.markReaderIndex();

        // 遍历 byteBuf 直至 0 结尾
        // 但是 '\0' 才代表是结束标志，才是真的 0；
        // '0' 的ASCII 值是48.
        byte terminatedChar = '\0';
        while (terminatedChar != buf.readByte()) {
            length++;
        }
        // 跳过 00 结束标志
        length++;

        // 重置读指针
        buf.resetReaderIndex();

        byte[] content = new byte[length];
        buf.readBytes(content);

        return new String(content, 0, length - 1);
    }

    /**
     * 写入 string<NUL> 类型的数据.
     * 此类数据没有长度限制，直至 00 结尾，不同数据包代表不同的数据.
     * 其实就是在最后，加一个 '0' 结束符
     * @param value 字符串
     */
    public static byte[] writeNullTerminatedString(String value) {
        return (value + "\0").getBytes();
    }

    /**
     * 对密码进行加密，使其满足 MySql411
     * @param password 密码
     * @param scramble 不知道是什么，反正加密要用到
     * @return 加密后的密码
     */
    public static byte[] encryptPasswordWithSha1(String password, String scramble) {

        if (StringUtils.isEmpty(password)) {
            return new byte[0];
        }

        // java.security 工具类
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // 1、SHA1(password)
        byte[] passwordSha1Encrypt = sha.digest(password.getBytes());

        // 2、SHA1(SHA1(password))
        byte[] passwordSha1DoubleEncrypt = sha.digest(passwordSha1Encrypt);

        // 3、SHA1(challenge + SHA1(SHA1(password)))：这里指的是拼接字节数组
        byte[] challengeAppendPasswordSha1DoubleEncrypt = byteArrAppend(scramble.getBytes(), passwordSha1DoubleEncrypt);
        byte[] appendedByteArrayEncrypt = sha.digest(challengeAppendPasswordSha1DoubleEncrypt);

        // 4、SHA1(password) ^ SHA1(challenge + SHA1(SHA1(password)))
        byte[] encryptResult = byteArrayXor(password.getBytes(), appendedByteArrayEncrypt);

        // 5、HEX(SHA1(password) ^ SHA1(challenge + SHA1(SHA1(password))))
        // 若已经是 byte[] 的，无需转换

        return encryptResult;
    }

    /**
     * byte数组拼接，主要是利用 arrayCopy 方法，这个方法在 java 源码中很常见
     * @param firstArr 第一个 byte 数组
     * @param secondArr 第二个 byte 数组
     * @return 拼接后的 byte 数组
     */
    private static byte[] byteArrAppend(byte[] firstArr, byte[] secondArr) {
        int firstArrLen = firstArr.length;
        int secondArrLen = secondArr.length;

        byte[] result = new byte[firstArrLen + secondArrLen];
        System.arraycopy(firstArr, 0, result, 0, firstArrLen);
        System.arraycopy(secondArr, 0, result, firstArrLen, secondArrLen);

        return result;
    }

    /**
     * byte数组抑或；
     *
     * 1、若两个数组的长度一致，则 first[i] ^ second[i]；
     * 2、若两个数组的长度不一致，则最少的那一方循环，直至最多的那一方遍历完，例如最多的是 first 数组：first[i] ^ second[i % second.length]
     *
     * @param firstArr 第一个 byte 数组
     * @param secondArr 第二个 byte 数组
     * @return 抑或后的 byte 数组
     */
    private static byte[] byteArrayXor(byte[] firstArr, byte[] secondArr) {

        int firstArrLen = firstArr.length;
        int secondArrLen = secondArr.length;

        byte[] result = new byte[Math.max(firstArrLen, secondArrLen)];

        if (firstArrLen > secondArrLen) {
            for (int i = 0; i < firstArr.length; i++) {
                result[i] = (byte) (firstArr[i] ^ secondArr[i % secondArrLen]);
            }

        } else {
            for (int i = 0; i < firstArr.length; i++) {
                result[i] = (byte) (secondArr[i] ^ firstArr[i % firstArrLen]);
            }
        }

        return result;
    }
}
