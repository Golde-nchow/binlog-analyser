package com.netty.binlog.constant;

import lombok.Getter;

/**
 * @author by chow
 * @Description 表-字段类型
 * @date 2021/4/11 下午3:29
 */
@Getter
public enum TableColumnType {

    /**
     *
     */
    DECIMAL(0),

    /**
     *
     */
    TINY(1),

    /**
     *
     */
    SHORT(2),

    /**
     *
     */
    LONG(3),

    /**
     *
     */
    FLOAT(4),

    /**
     *
     */
    DOUBLE(5),

    /**
     *
     */
    NULL(6),

    /**
     *
     */
    TIMESTAMP(7),

    /**
     *
     */
    LONGLONG(8),

    /**
     *
     */
    INT24(9),

    /**
     *
     */
    DATE(10),

    /**
     *
     */
    TIME(11),

    /**
     *
     */
    DATETIME(12),

    /**
     *
     */
    YEAR(13),

    /**
     *
     */
    NEWDATE(14),

    /**
     *
     */
    VARCHAR(15),

    /**
     *
     */
    BIT(16),

    /**
     *
     */
    TIMESTAMP_V2(17),

    /**
     *
     */
    DATETIME_V2(18),

    /**
     *
     */
    TIME_V2(19),

    /**
     *
     */
    JSON(245),

    /**
     *
     */
    NEWDECIMAL(246),

    /**
     *
     */
    ENUM(247),

    /**
     *
     */
    SET(248),

    /**
     *
     */
    TINY_BLOB(249),

    /**
     *
     */
    MEDIUM_BLOB(250),

    /**
     *
     */
    LONG_BLOB(251),

    /**
     *
     */
    BLOB(252),

    /**
     *
     */
    VAR_STRING(253),

    /**
     *
     */
    STRING(254),

    /**
     *
     */
    GEOMETRY(255),
    ;

    private int code;

    TableColumnType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * 通过 hex value，获取字段类型名称
     * @param code hex value
     * @return 类型名称
     */
    public static String getByCode(int code) {
        TableColumnType[] values = values();
        for (TableColumnType value : values) {
            if (value.code == code) {
                return value.name();
            }
        }
        return "";
    }
}
