package com.netty.binlog.constant;

/**
 * @author by chow
 * @Description MySQL 发送的命令类型
 * @date 2021/3/23 下午9:40
 */
public enum CommandTypes {

    /**
     * 内部服务器命令-休眠.
     */
    SLEEP,

    /**
     * 用于通知服务器客户端要关闭连接.
     */
    QUIT,

    /**
     * 用于更改连接的默认数据库.
     */
    INIT_DB,

    /**
     * 用于向服务器发送立即执行的基于文本的查询.
     */
    QUERY,

    /**
     * 用于获取特定表的列定义.
     */
    FIELD_LIST,

    /**
     * 用于创建新数据库.
     */
    CREATE_DB,

    /**
     * 用于删除数据库.
     */
    DROP_DB,

    /**
     * FLUSH ... 和 RESET ... 的低版本实现.
     */
    REFRESH,

    /**
     * 用于关闭 mysql 服务器.
     */
    SHUTDOWN,

    /**
     * 用于获取可供人阅读的内部统计字符串.
     */
    STATISTICS,

    /**
     * 用于获取活动线程的列表.
     */
    PROCESS_INFO,

    /**
     * 内部服务器命令-连接.
     */
    CONNECT,

    /**
     * 用于请求服务器终止连接.
     */
    PROCESS_KILL,

    /**
     * 将内部调试信息转储到 mysql 服务器的 stdout.
     */
    DEBUG,

    /**
     * 用于检查服务器是否处于活动状态.
     */
    PING,

    /**
     * 内部服务器命令-定时.
     */
    TIME,

    /**
     * 内部服务器命令-延迟插入.
     */
    DELAYED_INSERT,

    /**
     * 用于更改当前连接的用户并重置连接状态.
     */
    CHANGE_USER,

    /**
     * 从给定位置开始从主机请求二进制日志网络流.
     */
    BINLOG_DUMP,

    /**
     * 用于转储特定表.
     */
    TABLE_DUMP,

    /**
     * 内部服务器命令-连接超时.
     */
    CONNECT_OUT,

    /**
     * 在主服务器上注册一个从服务器。应在请求二进制日志事件之前发送 {@link #BINLOG_DUMP}.
     */
    REGISTER_SLAVE,

    /**
     * 从传递的查询字符串创建: 预处理语句.
     */
    STMT_PREPARE,

    /**
     * 用于执行由语句id标识的预处理语句.
     */
    STMT_EXECUTE,

    /**
     * 用于发送列的一些数据.
     */
    STMT_SEND_LONG_DATA,

    /**
     * 取消分配预处理语句.
     */
    STMT_CLOSE,

    /**
     * 重置那些，被 {@link #STMT_SEND_LONG_DATA} 命令积累的预处理语句的数据.
     */
    STMT_RESET,

    /**
     * 允许为当前连接启用和禁用多语句（ MULTI_STATEMENTS ）
     */
    SET_OPTION,

    /**
     * 在执行 {@link #STMT_EXECUTE} 后，从现有的结果集中获取一行数据.
     */
    STMT_FETCH,

    /**
     * 内部服务器命令-守护
     */
    DAEMON,

    /**
     * 用于基于GTID请求二进制日志网络流.
     */
    BINLOG_DUMP_GTID



}
