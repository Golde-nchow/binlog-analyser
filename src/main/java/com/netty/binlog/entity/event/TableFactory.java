package com.netty.binlog.entity.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by chow
 * @Description 数据表工厂类
 * @date 2021/4/10 下午4:22
 */
public class TableFactory {

    /**
     * 全局数据表暂存
     */
    private static Map<Long, TableMapData> tables = new ConcurrentHashMap<>();

    /**
     * 通过 table_id，获取数据表结构
     * @param tableId 表 id
     * @return 数据表结构
     */
    public static synchronized TableMapData getByTableId(Long tableId) {
        return tables.get(tableId);
    }

    /**
     * 暂存数据表结构
     * @param tableMapData 表结构
     */
    public static synchronized void setByTable(TableMapData tableMapData) {
        tables.put(tableMapData.getTableId(), tableMapData);
    }

}
