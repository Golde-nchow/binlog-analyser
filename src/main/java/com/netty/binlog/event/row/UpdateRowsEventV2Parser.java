package com.netty.binlog.event.row;

import com.netty.binlog.constant.RowsEventFlags;
import com.netty.binlog.entity.event.rows.AbstractRowsEventDataParser;
import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.event.IEventParser;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author by chow
 * @Description 行更新事件-V2-解析器
 * @date 2021/4/13 下午8:18
 */
public class UpdateRowsEventV2Parser extends AbstractRowsEventDataParser implements IEventParser {

    /**
     * 执行解析
     * @param eventHeader 事件头部信息，某些字段需要通过包长度计算
     * @param content     缓冲区
     */
    @Override
    public void parse(EventHeader eventHeader, ByteBuf content) {

        // ======================== 头部信息 ===============================
        // 6字节：table_id
        int tableId = ByteUtil.readInt(content, 6);

        // 2字节：flags
        int flags = ByteUtil.readInt(content, 2);

        // 2字节：extra_data_len
        int extraDataLen = ByteUtil.readInt(content, 2);

        // extraDataLen 字节：extra_data [length=extra_data_len - 2]
        // 也就是说，若 extra_data_len <= 2，则没有 extra_data
        int availableExtraDataLen = extraDataLen - 2;
        byte[] extraData = new byte[availableExtraDataLen];
        if (availableExtraDataLen > 0) {
            extraData = ByteUtil.readString(content, availableExtraDataLen).getBytes();
        }

        // ======================== 内容信息 ===============================
        // int<lenenc>：列的数量
        int columnCount = ByteUtil.readLenencInt(content);

        // string<var>：columns-present-bitmap1；长度：length: (num of columns+7)/8
        // 若为 ff，则代表使用了全部字段
        BitSet columnsPresentBitmap1 = ByteUtil.readBitSet(content, columnCount);

        // string<var>：columns-present-bitmap2；字节长度：(num of columns+7)/8
        BitSet columnsPresentBitmap2 = ByteUtil.readBitSet(content, columnCount);

        // ======================== 行信息 ===============================

        // bitmap：null bitmap；(length(columns-present-bitmap1) + 7) / 8

        // 通过字段的类型，获取表视图1中，每个内容不为空的字段占用的字节，从而获取对应值

        // 更新事件多了两个数据
        // bitmap：null bitmap；(length(columns-present-bitmap2) + 7) / 8

        // 通过字段的类型，获取表视图2中，每个内容不为空的字段占用的字节，从而获取对应值

        // ======================== 打印信息 ===============================

        System.out.println("RBR-V2-更新事件-头部：tableId：" + tableId);
        System.out.println("RBR-V2-更新事件-头部：flags：" + RowsEventFlags.getDesc(flags));
        System.out.println("RBR-V2-更新事件-头部：extraDataLen：" + extraDataLen);
        System.out.println("RBR-V2-更新事件-头部：extraData：" + Arrays.toString(extraData));

        System.out.println("RBR-V2-更新事件-内容：列数量：" + columnCount);
        System.out.println("RBR-V2-更新事件-内容：columns-present-bitmap1：" + Arrays.toString(parseBitSetToArr(columnsPresentBitmap1, columnCount)));
        System.out.println("RBR-V2-更新事件-内容：columns-present-bitmap2：" + Arrays.toString(parseBitSetToArr(columnsPresentBitmap2, columnCount)));

        System.out.println("RBR-V2-更新事件-行信息：");
        System.out.println("bitmap1 ========================");
        parseRow(content, tableId);
        System.out.println("bitmap2 ========================");
        parseRow(content, tableId);

        System.out.println("======================== RBR-V2-更新事件解析完成 ===========================");
    }
}
