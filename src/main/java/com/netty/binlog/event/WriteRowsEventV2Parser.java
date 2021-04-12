package com.netty.binlog.event;

import com.netty.binlog.constant.RowsEventFlags;
import com.netty.binlog.entity.event.rows.AbstractRowsEventDataParser;
import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author by chow
 * @Description MySQL 5.6+ 的 RBR 模式写入事件
 * @date 2021/4/6 下午9:17
 */
public class WriteRowsEventV2Parser extends AbstractRowsEventDataParser implements IEventParser {

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
        BitSet columnsPresentBitmap = ByteUtil.readBitSet(content, columnCount);

        // ======================== 行信息 ===============================
        // bitmap：extra-data：空视图；(length(columns-present-bitmap1) + 7) / 8

        // 表视图的每个列的值
        // bitmap：空视图；(length(columns-present-bitmap2) + 7) / 8

        System.out.println("RBR-V2-写事件-头部：tableId：" + tableId);
        System.out.println("RBR-V2-写事件-头部：flags：" + RowsEventFlags.getDesc(flags));
        System.out.println("RBR-V2-写事件-头部：extraDataLen：" + extraDataLen);
        System.out.println("RBR-V2-写事件-头部：extraData：" + Arrays.toString(extraData));

        System.out.println("RBR-V2-写事件-内容：列数量：" + columnCount);
        System.out.println("RBR-V2-写事件-内容：columns-present-bitmap：" + Arrays.toString(parseBitSetToArr(columnsPresentBitmap, columnCount)));

        System.out.println("RBR-V2-写事件-行信息：");
        parseRow(content, tableId);

        System.out.println("======================== RBR-V2-写事件解析完成 ===========================");
    }
}
