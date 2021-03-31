package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description binlog 事件头部标志位
 * @date 2021/3/30 上午12:42
 */
public enum BinlogEventHeaderFlags {

    /**
     * 如果查询依赖于线程（例如：临时表）。
     * 目前 mysqlbinlog 使用它来知道，它必须在查询之前打印 "SET@PSEUDO_THREAD_ID=xx；"
     * （为每个查询打印它不会有什么坏处，但这会很慢）。
     */
    LOG_EVENT_THREAD_SPECIFIC_F(4),

    /**
     * 禁止在实际语句之前生成 "USE" 语句。
     * 应该为不需要当前数据库集才能正常工作的任何事件设置此标志。
     * 最著名的例子是 "创建数据库" 和 "删除数据库"。
     */
    LOG_EVENT_SUPPRESS_USE_F(8),

    /**
     * 人工事件是随意创建的，不会写入二进制文件日志。
     * 这些当从属SQL线程执行事件时，事件不应更新主日志位置。
     */
    LOG_EVENT_ARTIFICIAL_F(32),

    /**
     * 设置了此标志的事件由从IO线程创建并写入中继日志
     */
    LOG_EVENT_RELAY_LOG_F(64),

    /**
     * 对于从设备 "s" 无法识别的带有类型代码的事件 "e"，
     * "s" 将检查 "e" 中是否存在 "LOG_EVENT_IGNORABLE_F"，
     * 如果设置了该标志，则忽略 "e"。否则，"s"确认在中继日志中发现未知事件。
     */
    LOG_EVENT_IGNORABLE_F(128),

    /**
     * 具有此标志的事件不会被过滤（例如，在当前数据库上），
     * 并且总是写入二进制日志，而不考虑过滤器。
     */
    LOG_EVENT_NO_FILTER_F(256),

    /**
     * 可以对事件组进行标记，
     * 以强制其在与任何其他工作者隔离的情况下执行。
     */
    LOG_EVENT_MTS_ISOLATE_F(512),

    ;

    /**
     * 描述
     */
    private int value;

    BinlogEventHeaderFlags(int value) {
    }
}
