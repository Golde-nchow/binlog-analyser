package com.netty.binlog.event;

import com.netty.binlog.constant.TableColumnMetaLen;
import com.netty.binlog.entity.event.TableFactory;
import com.netty.binlog.entity.event.TableMapData;
import com.netty.binlog.entity.event.TableMapMetaData;
import com.netty.binlog.entity.pack.EventHeader;
import com.netty.binlog.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author by chow
 * @Description 数据表事件解析器
 *
 * 官方文档：https://dev.mysql.com/doc/internals/en/table-map-event.html
 *
 * @date 2021/4/10 下午5:30
 */
@Data
public class TableMapEventParser implements IEventParser {

    /**
     * 执行解析
     * @param eventHeader 事件头部信息，某些字段需要通过包长度计算
     * @param content     缓冲区
     */
    @Override
    public void parse(EventHeader eventHeader, ByteBuf content) {
        // 6字节：table_id
        int tableId = ByteUtil.readInt(content, 6);

        // 2字节：flags
        int flags = ByteUtil.readInt(content, 2);

        // 1字节：数据库名称长度
        // n字节：数据库名称
        int databaseLen = ByteUtil.readInt(content, 1);
        String database = ByteUtil.readString(content, databaseLen);

        // 1字节：0x00
        content.skipBytes(1);

        // 1字节：数据表名称长度
        // n字节：数据表名称
        int tableLen = ByteUtil.readInt(content, 1);
        String table = ByteUtil.readString(content, tableLen);

        // 1字节：0x00
        content.skipBytes(1);

        // int<lenenc>：列数量
        int columnCount = ByteUtil.readLenencInt(content);

        // 列数量长度字节（n个1字节）：定义的列类型
        byte[] defTypeArr = ByteUtil.readString(content, columnCount).getBytes();

        // 列数量长度字节（n个1字节）：定义的列长度
        int[] metaDataDefLenArr = readMetaDataDefLen(content, defTypeArr);

        // null_bitmap[len=(列数量长度 + 8) / 7]：若某个列可为 NULL，则记录 1
        BitSet nullBitmap = ByteUtil.readBitSet(content, columnCount);

        // 放入全局信息
        TableMapData tableMapData = new TableMapData();
        tableMapData.setTableId(tableId);
        tableMapData.setDatabase(database);
        tableMapData.setTable(table);
        TableMapMetaData tableMapMetaData = new TableMapMetaData();
        tableMapMetaData.setColumnTypes(defTypeArr);
        tableMapMetaData.setNullBitMap(nullBitmap);
        tableMapMetaData.setColumnMetadataLen(metaDataDefLenArr);
        TableFactory.setByTable(tableMapData);

        System.out.println("表id：" + tableId);
        System.out.println("flags：" + flags);
        System.out.println("数据库名称：" + database);
        System.out.println("数据表名称：" + table);
        System.out.println("定义的列类型：" + Arrays.toString(defTypeArr));
        System.out.println("定义的列长度：" + Arrays.toString(metaDataDefLenArr));

        boolean[] nullBitmapArr = new boolean[columnCount];
        for (int i = 0; i < columnCount; i++) {
            boolean canBeNull = nullBitmap.get(i);
            nullBitmapArr[i] = canBeNull;
        }
        System.out.println("null bitmap：" + Arrays.toString(nullBitmapArr));

        System.out.println("==================== 数据表事件解析完成 ===================");
    }

    /**
     * 读取各个列的定义长度
     * @param defTypeArr 列类型
     * @param byteBuf 缓冲区
     * @return 各个列定义长度
     */
    private int[] readMetaDataDefLen(ByteBuf byteBuf, byte[] defTypeArr) {

        int index = 0;
        int[] metadataDefLen = new int[defTypeArr.length];

        int metaDataDefCount = ByteUtil.readInt(byteBuf, 1);
        if (metaDataDefCount == 0) {
            return metadataDefLen;
        }

        for (byte b : defTypeArr) {
            int len = TableColumnMetaLen.getMetaLenByCode(b);
            metadataDefLen[index] = len == 0 ? 0 : ByteUtil.readInt(byteBuf, len);
            index++;
        }

        return metadataDefLen;
    }

}
