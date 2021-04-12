package com.netty.binlog.entity.event.rows;

import com.netty.binlog.column.IColumnParser;
import com.netty.binlog.column.factory.ColumnParserFactory;
import com.netty.binlog.entity.event.TableFactory;
import com.netty.binlog.entity.event.TableMapData;
import com.netty.binlog.entity.event.TableMapMetaData;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.BitSet;

/**
 * @author by chow
 * @Description 行事件数据解析器
 * @date 2021/4/10 下午4:00
 */
public abstract class AbstractRowsEventDataParser {

    /**
     * 解析列数据
     * @param byteBuf byteBuf 缓冲区
     * @param tableId table_id
     */
    protected void parseRow(ByteBuf byteBuf, Integer tableId) {
        TableMapData tableMapData = TableFactory.getByTableId(tableId);
        TableMapMetaData tableMapMetadata = tableMapData.getTableMapMetadata();
        int[] columnMetadataLen = tableMapData.getTableMapMetadata().getColumnMetadataLen();

        // 各个字段类型
        byte[] columnTypes = tableMapMetadata.getColumnTypes();
        // null-column-bitmap
        // 若不位 null，则在 bitmap 中是 0
        BitSet nullColumnBitmap = ByteUtil.readBitSet(byteBuf, columnTypes.length);

        for (int i = 0; i < columnTypes.length; i++) {
            // 字段类型
            int type = columnTypes[i];
            // 字段定义的长度
            int metaDefLen = columnMetadataLen[i];
            // 判断字段是否为空
            if (nullColumnBitmap.get(i)) {
                continue;
            }

            IColumnParser columnParser = ColumnParserFactory.getParserByColumnType(type);
            if (columnParser != null) {
                Object columnValue = columnParser.parser(byteBuf, metaDefLen);
                System.out.println("第 " + (i + 1) + " 个字段的值为：" + columnValue);
            }
        }
    }

    /**
     * 将 bitSet 转换为 boolean 数组
     * @param bitSet bitSet
     * @param columnCount 列数量
     * @return boolean 数组
     */
    protected boolean[] parseBitSetToArr(BitSet bitSet, int columnCount) {
        boolean[] nullBitmapArr = new boolean[columnCount];
        for (int i = 0; i < columnCount; i++) {
            boolean canBeNull = bitSet.get(i);
            nullBitmapArr[i] = canBeNull;
        }
        return nullBitmapArr;
    }

}
