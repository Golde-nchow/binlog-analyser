package com.netty.binlog.entity.event;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by chow
 * @Description 表数据结构
 * @date 2021/4/10 下午4:15
 */
@Data
public class TableMapData implements Serializable {

    /**
     * 表 id
     */
    private Integer tableId;

    /**
     * 所在数据库
     */
    private String database;

    /**
     * 数据表名称
     */
    private String table;

    /**
     * 列数据
     */
    private TableMapMetaData tableMapMetadata;

}
