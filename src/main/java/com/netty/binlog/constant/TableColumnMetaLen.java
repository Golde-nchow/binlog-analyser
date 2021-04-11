package com.netty.binlog.constant;

import lombok.Getter;

/**
 * @author by chow
 * @Description 表-字段类型-meta长度
 * @date 2021/4/11 下午3:37
 */
@Getter
public enum TableColumnMetaLen {

    /**
     *
     */
    DECIMAL(TableColumnType.DECIMAL.getCode(), 2),

    /**
     *
     */
    TINY(TableColumnType.TINY.getCode(), 0),

    /**
     *
     */
    SHORT(TableColumnType.SHORT.getCode(), 0),

    /**
     *
     */
    LONG(TableColumnType.LONG.getCode(), 0),

    /**
     *
     */
    FLOAT(TableColumnType.FLOAT.getCode(), 1),

    /**
     *
     */
    DOUBLE(TableColumnType.DOUBLE.getCode(), 1),

    /**
     *
     */
    NULL(TableColumnType.NULL.getCode(), 0),

    /**
     *
     */
    TIMESTAMP(TableColumnType.TIMESTAMP.getCode(), 0),

    /**
     *
     */
    LONGLONG(TableColumnType.LONGLONG.getCode(), 0),

    /**
     *
     */
    INT24(TableColumnType.INT24.getCode(), 0),

    /**
     *
     */
    DATE(TableColumnType.DATE.getCode(), 0),

    /**
     *
     */
    TIME(TableColumnType.TIME.getCode(), 0),

    /**
     *
     */
    DATETIME(TableColumnType.DATETIME.getCode(), 0),

    /**
     *
     */
    VARCHAR(TableColumnType.VARCHAR.getCode(), 2),

    /**
     *
     */
    BIT(TableColumnType.BIT.getCode(), 0),

    /**
     *
     */
    TIMESTAMP_V2(TableColumnType.DECIMAL.getCode(), 1),

    /**
     *
     */
    DATETIME_V2(TableColumnType.DECIMAL.getCode(), 1),

    /**
     *
     */
    TIME_V2(TableColumnType.DECIMAL.getCode(), 1),

    /**
     *
     */
    JSON(TableColumnType.JSON.getCode(), 1),

    /**
     *
     */
    NEWDECIMAL(TableColumnType.DECIMAL.getCode(), 2),

    /**
     *
     */
    ENUM(TableColumnType.ENUM.getCode(), 2),

    /**
     *
     */
    SET(TableColumnType.DECIMAL.getCode(), 2),

    /**
     *
     */
    BLOB(TableColumnType.DECIMAL.getCode(), 1),

    /**
     *
     */
    VAR_STRING(TableColumnType.VAR_STRING.getCode(), 2),

    /**
     *
     */
    STRING(TableColumnType.STRING.getCode(), 2),

    /**
     *
     */
    GEOMETRY(TableColumnType.DECIMAL.getCode(), 2),
    ;

    /**
     * 字段的 hex value
     */
    private Integer code;

    /**
     * 字段类型的长度
     */
    private Integer length;

    TableColumnMetaLen(Integer code, Integer length) {
        this.code = code;
        this.length = length;
    }

    /**
     * 通过 hex value 获取 meta 长度
     * @param code hex value
     * @return meta 长度
     */
    public static int getMetaLenByCode(int code) {
        TableColumnMetaLen[] values = values();
        for (TableColumnMetaLen value : values) {
            if (value.code == code) {
                return value.length;
            }
        }
        return 0;
    }
}
