package com.netty.binlog.entity.event;

import lombok.Data;

import java.util.BitSet;

/**
 * @author by chow
 * @Description 数据表-元数据（列数据）
 * @date 2021/4/10 下午4:28
 */
@Data
public class TableMapMetaData {

    /**
     * 列名称
     */
    private String[] columns;

    /**
     * 列类型
     */
    private byte[] columnTypes;

    /**
     * 列类型数据所占的长度
     */
    private int[] columnMetadataLen;

    /**
     * 若某个字段可为空，那么在 bitMap 上就是 0
     */
    private BitSet nullBitMap;

}
