package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description 字段类型
 * @date 2021/3/26 下午9:48
 */
public enum FieldType {

    /**
     * Decimal 类型
     */
    MYSQL_TYPE_DECIMAL("decimal",222),

    /**
     * tiny 类型
     */
    MYSQL_TYPE_TINY("tiny",223),

    /**
     * short 类型
     */
    MYSQL_TYPE_SHORT("short",224),

    /**
     * long 类型
     */
    MYSQL_TYPE_LONG("long",225),

    /**
     * float 类型
     */
    MYSQL_TYPE_FLOAT("float",226),

    /**
     * double 类型
     */
    MYSQL_TYPE_DOUBLE("double",227),

    /**
     * null 类型
     */
    MYSQL_TYPE_NULL("null",228),

    /**
     * timestamp 类型
     */
    MYSQL_TYPE_TIMESTAMP("timestamp",229),

    /**
     * long long 类型
     */
    MYSQL_TYPE_LONGLONG("long long",230),

    /**
     * int 类型
     */
    MYSQL_TYPE_INT24("int",231),

    /**
     * date 类型
     */
    MYSQL_TYPE_DATE("date",232),

    /**
     * time 类型
     */
    MYSQL_TYPE_TIME("time",233),

    /**
     * datetime 类型
     */
    MYSQL_TYPE_DATETIME("datetime",234),

    /**
     * year 类型
     */
    MYSQL_TYPE_YEAR("year",235),

    /**
     * new date 类型（内部使用的类型）
     */
    MYSQL_TYPE_NEWDATE("new date",236),

    /**
     * varchar 类型
     */
    MYSQL_TYPE_VARCHAR("varchar",237),

    /**
     * bit 类型
     */
    MYSQL_TYPE_BIT("bit",238),

    /**
     * timestamp2 类型（内部使用的类型）
     */
    MYSQL_TYPE_TIMESTAMP2("timestamp2",239),

    /**
     * datetime2 类型（内部使用的类型）
     */
    MYSQL_TYPE_DATETIME2("datetime2",240),

    /**
     * time2 类型（内部使用的类型）
     */
    MYSQL_TYPE_TIME2("time2",241),

    /**
     * array 类型
     */
    MYSQL_TYPE_TYPED_ARRAY("array",242),

    /**
     * invalid 类型
     */
    MYSQL_TYPE_INVALID("invalid",243),

    /**
     * bool 类型
     */
    MYSQL_TYPE_BOOL("bool",244),

    /**
     * json 类型
     */
    MYSQL_TYPE_JSON("json",245),

    /**
     * new decimal 类型
     */
    MYSQL_TYPE_NEW_DECIMAL("new decimal",246),

    /**
     * enum 类型
     */
    MYSQL_TYPE_ENUM("enum",247),

    /**
     * set 类型
     */
    MYSQL_TYPE_SET("set",248),

    /**
     * tiny blob 类型
     */
    MYSQL_TYPE_TINY_BLOB("tiny blob",249),

    /**
     * medium blob 类型
     */
    MYSQL_TYPE_MEDIUM_BLOB("medium blob",250),

    /**
     * long blob 类型
     */
    MYSQL_TYPE_LONG_BLOB("long blob",251),

    /**
     * blob 类型
     */
    MYSQL_TYPE_BLOB("blob",252),

    /**
     * var string 类型
     */
    MYSQL_TYPE_VAR_STRING("var string",253),

    /**
     * string 类型
     */
    MYSQL_TYPE_STRING("string",254),

    /**
     * geometry 类型
     */
    MYSQL_TYPE_GEOMETRY("geometry", 255),

    ;

    /**
     * 字段描述
     */
    private String desc;

    /**
     * 字段的值
     */
    private Integer value;

    /**
     * 10进制值
     */
    FieldType(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }

    /**
     * 通过 value 获取 desc
     * @return 字段描述
     */
    public static String get(int value) {
        FieldType[] fieldTypes = values();
        for (FieldType fieldType : fieldTypes) {
            if (value == fieldType.value) {
                return fieldType.desc;
            }
        }
        return "";
    }
}
