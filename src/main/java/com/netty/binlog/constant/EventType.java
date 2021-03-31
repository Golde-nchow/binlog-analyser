package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description 事件类型
 *
 * 不知道为什么，在 8.0.23 文档里面是没有的
 *
 * @date 2021/3/30 下午11:12
 */
public enum EventType {

    /**
     * 未知事件
     */
    UNKNOWN_EVENT,

    /**
     * 写入每个二进制日志文件开头的描述符事件。
     * （在MySQL4.0和4.1中，此事件仅写入服务器启动后创建的第一个 binlog 文件。）
     * 此事件在 MySQL3.23 到 4.1 中使用，在 MySQL5.0中 由 FORMAT_DESCRIPTION 取代。
     */
    START_EVENT_V3,

    /**
     * 在更新语句完成时写入
     */
    QUERY_EVENT,

    /**
     * MYSQL 停止时写入
     */
    STOP_EVENT,

    /**
     * 当 binlog 文件大小上限后，需要切换 binlog 文件时触发
     */
    ROTATE_EVENT,

    /**
     * 每次使用 AUTO_INCREMENT 列，或者 LAST_INSERT_ID 函数时。
     * 在其他语句之前，但只写在 QUERY 事件之前，不适用于 RBR。
     */
    INTVAR_EVENT,

    /**
     * 在 MySQL 3.23，用于执行 LOAD DATA INFILE 语句
     */
    LOAD_EVENT,

    /**
     * 从属事件
     */
    SLAVE_EVENT,

    CREATE_FILE_EVENT,
    APPEND_BLOCK_EVENT,
    EXEC_LOAD_EVENT,
    DELETE_FILE_EVENT,
    NEW_LOAD_EVENT,
    RAND_EVENT,
    USER_VAR_EVENT,

    /**
     * 写入每个二进制日志文件开头的描述符事件。
     * 此事件从MySQL5.0开始使用；它取代 START_EVENT_V3。
     */
    FORMAT_DESCRIPTION_EVENT,
    XID_EVENT,
    BEGIN_LOAD_QUERY_EVENT,
    EXECUTE_LOAD_QUERY_EVENT,
    TABLE_MAP_EVENT,
    PRE_GA_WRITE_ROWS_EVENT,
    PRE_GA_UPDATE_ROWS_EVENT,
    PRE_GA_DELETE_ROWS_EVENT,
    WRITE_ROWS_EVENT,
    UPDATE_ROWS_EVENT,
    DELETE_ROWS_EVENT,
    INCIDENT_EVENT,
    HEARTBEAT_LOG_EVENT,
    IGNORABLE_LOG_EVENT,
    ROWS_QUERY_LOG_EVENT,
    EXT_WRITE_ROWS_EVENT,
    EXT_UPDATE_ROWS_EVENT,
    EXT_DELETE_ROWS_EVENT,
    GTID_LOG_EVENT,
    ANONYMOUS_GTID_LOG_EVENT,
    PREVIOUS_GTIDS_LOG_EVENT,
    ENUM_END_EVENT

    ;

}
