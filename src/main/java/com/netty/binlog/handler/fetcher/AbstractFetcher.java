package com.netty.binlog.handler.fetcher;

import com.netty.binlog.constant.CharsetConstant;
import com.netty.binlog.constant.FieldType;
import com.netty.binlog.constant.ProtocolStatusFlags;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author by chow
 * @Description 获取查询结果接口逻辑
 * @date 2021/3/28 下午2:12
 */
@Data
public abstract class AbstractFetcher {

    /**
     * 上下文
     */
    private ChannelHandlerContext ctx;

    /**
     * 默认实现：获取列数量
     * @param content netty 缓冲区
     * @return 列数量
     */
    public int getColumnCount(ByteBuf content) {
        // int<lenenc>：列数量
        int result = ByteUtil.readLenencInt(content);
        System.out.println("列数量：" + result);

        return result;
    }

    /**
     * 获取列的信息，不包括值
     * @param content netty 缓冲区
     * @param fields 列数据
     */
    public void getColumn(ByteBuf content, Map<String, String> fields) {
        // 1、string<lenenc>：catalog 目录，目前一直是 def
        String catalog = ByteUtil.readLenencString(content);

        // 2、string<lenenc>：数据库
        String schema = ByteUtil.readLenencString(content);

        // 3、string<lenenc>：逻辑表名（virtual table name）
        String table = ByteUtil.readLenencString(content);

        // 4、string<lenenc>：物理表名（physical table name）
        String orgTable = ByteUtil.readLenencString(content);

        // 5、string<lenenc>：字段逻辑名称
        String name = ByteUtil.readLenencString(content);

        // 6、string<lenenc>：字段物理名称
        String orgName = ByteUtil.readLenencString(content);

        // 7、int<lenenc>：定长字段的长度（0x0c）
        int fixLengthFields = ByteUtil.readLenencInt(content);

        // 8、int<2>：编码
        int characterSet = ByteUtil.readInt(content, 2);

        // 9、int<4>：列长度
        int columnLength = ByteUtil.readInt(content, 4);

        // 10、int<1>：字段类型
        int type = ByteUtil.readInt(content, 1);

        // 11、int<2>：flags 字段标志
        int flags = ByteUtil.readInt(content, 2);

        // 12、int<1>：最大显示的小数位
        // 0x00：整数、字符串
        // 0x1f：动态字符串、double、float
        // 0x00 - 0x51：小数
        int maxShownDecimalDigits = ByteUtil.readInt(content, 1);

        System.out.println("目录：" + catalog);
        System.out.println("数据库：" + schema);
        System.out.println("逻辑表名：" + table);
        System.out.println("物理表名：" + orgTable);
        System.out.println("字段逻辑名称：" + name);
        System.out.println("字段物理名称：" + orgName);
        System.out.println("定长字段的长度：" + fixLengthFields);
        System.out.println("编码：" + CharsetConstant.get(characterSet - 1));
        System.out.println("列长度：" + columnLength);
        System.out.println("字段类型：" + FieldType.get(type));
        System.out.println("flags 字段标志：" + flags);
        System.out.println("最大显示的小数位：" + maxShownDecimalDigits);

        // 将字段暂存在 Map
        fields.put(name, String.valueOf(fields.size() + 1));
    }

    /**
     * 获取列信息的 EOF 包，表示结束
     * @param content netty 缓冲区
     */
    public void getMetaDataEof(ByteBuf content) {
        // Header；1字节：0xFE
        content.skipBytes(1);

        // warnings；2字节：警告数量；
        int warningNumbers = ByteUtil.readInt(content, 2);

        // status_flags；2字节；
        int statusFlags = ByteUtil.readInt(content, 2);

        System.out.println("警告数量：" + warningNumbers);
        System.out.println("状态：" + ProtocolStatusFlags.getStatusFlagsDescription(statusFlags));
    }

    /**
     * 获取列的值
     * @param content netty 缓冲区
     * @param columnCount 列数量
     * @param fields 列数据
     */
    public void getColumnValue(ByteBuf content,
                                int columnCount,
                                Map<String, String> fields) {
        int index = 1;
        while (index < columnCount) {
            String fieldValue = ByteUtil.readLenencString(content);
            System.out.println("第"+ (index) + "个字段的值：" + fieldValue);

            // 将 binlog 数据设置成 key-value
            Set<Map.Entry<String, String>> entries = fields.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (String.valueOf(index).equals(value)) {
                    entry.setValue(fieldValue);
                }
            }

            index++;
        }
    }

    /**
     * 获取列值的 EOF 数据报
     * @param content netty 缓冲区
     */
    public void getColumnValueEof(ByteBuf content) {
        // Header；1字节：0xFE
        content.skipBytes(1);

        // warnings；2字节：警告数量；
        int warningNumbers = ByteUtil.readInt(content, 2);

        // status_flags；2字节；
        int statusFlags = ByteUtil.readInt(content, 2);

        System.out.println("警告数量：" + warningNumbers);
        System.out.println("状态：" + ProtocolStatusFlags.getStatusFlagsDescription(statusFlags));
    }
}
