package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description 行事件的 flags 描述枚举
 * @date 2021/4/10 上午11:44
 */
public enum RowsEventFlags {

    /**
     * 空，占用位
     */
    EMPTY("占用位"),

    /**
     * 表达式结束
     */
    END_OF_STATEMENT("表达式结束"),

    /**
     * 没有外键检查
     */
    NO_FOREIGN_KEY_CHECKS("没有外键检查"),

    /**
     * 没有唯一约束检查
     */
    NO_UNIQUE_KEY_CHECKS("没有唯一约束检查"),

    /**
     * 有列数据
     */
    ROW_HAS_A_COLUMNS("有列数据"),

    ;

    /**
     * 描述
     */
    private String desc;

    RowsEventFlags(String desc) {
        this.desc = desc;
    }

    /**
     * 根据 key 获取描述
     * @param index 索引
     * @return 描述
     */
    public String getDesc(Integer index) {
        RowsEventFlags[] values = values();
        return values[index].desc;
    }
}
