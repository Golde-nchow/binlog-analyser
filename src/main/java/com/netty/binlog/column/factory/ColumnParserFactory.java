package com.netty.binlog.column.factory;

import com.netty.binlog.column.IColumnParser;
import com.netty.binlog.column.LongColumnParser;
import com.netty.binlog.column.VarcharColumnParser;
import com.netty.binlog.constant.TableColumnType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by chow
 * @Description 字段解析器工厂
 * @date 2021/4/11 下午10:06
 */
public class ColumnParserFactory {

    /**
     * 字段解析器 map
     */
    private static Map<Integer, IColumnParser> columnParserMap = new ConcurrentHashMap<>();

    static {
        columnParserMap.put(TableColumnType.VARCHAR.getCode(), new VarcharColumnParser());
        columnParserMap.put(TableColumnType.LONG.getCode(), new LongColumnParser());
    }

    /**
     * 通过字段类型，获取对应解析器
     * @param columnType 字段类型
     * @return 字段解析器
     */
    public static IColumnParser getParserByColumnType(Integer columnType) {
        return columnParserMap.get(columnType);
    }

}
