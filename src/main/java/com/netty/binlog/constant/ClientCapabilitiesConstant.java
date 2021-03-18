package com.netty.binlog.constant;

/**
 * 客户端权能常量 Capability Flags：
 * 官方文档：http://dev.mysql.com/doc/internals/en/capability-flags.html#packet-Protocol::CapabilityFlags
 */
public interface ClientCapabilitiesConstant {

    /**
     * 使用改进版的旧密码身份验证
     */
    int LONG_PASSWORD = 1;

    /**
     * 发送找到的行，而不是受影响的行
     */
    int FOUND_ROWS = 1 << 1;

    /**
     * ColumnDefinition320 协议的 long_flag
     */
    int LONG_FLAG = 1 << 2;

    /**
     * 可以在握手响应包中的连接上指定数据库名称。
     */
    int CONNECT_WITH_DB = 1 << 3;

    /**
     * 不允许 database.table.column 的形式
     */
    int NO_SCHEMA = 1 << 4;

    /**
     * 支持压缩协议
     */
    int COMPRESS = 1 << 5;

    /**
     * ODBC客户端行为的特殊处理
     */
    int ODBC = 1 << 6;

    /**
     * 可以使用 load 本地 sql 数据文件
     */
    int LOCAL_FILES = 1 << 7;

    /**
     * 分析器可以忽略 '(' 之前的空格
     */
    int IGNORE_SPACE = 1 << 8;

    /**
     * 新的 4.1 协议
     */
    int PROTOCOL_41 = 1 << 9;

    /**
     * 支持交互式和非交互式客户端
     */
    int INTERACTIVE = 1 << 10;

    /**
     * 发送权能标志后（握手后），切换到 ssl
     */
    int SSL = 1 << 11;

    /**
     * 如果发生网络故障，不要发送 SIGPIPE（仅限lib mysql client）
     */
    int IGNORE_SIGPIPE = 1 << 12;

    /**
     * 可以在 EOF 数据包中发送状态标志
     */
    int TRANSACTIONS = 1 << 13;

    /**
     * 在 4.1.0 中被命名为：CLIENT_PROTOCOL_41 协议
     */
    int RESERVED = 1 << 14;

    /**
     * 支持 Authentication::Native41 认证协议
     */
    int SECURE_CONNECTION = 1 << 15;

    /**
     * 可以处理每个查询、预处理的多个语句
     */
    int MULTI_STATEMENTS = 1 << 16;

    /**
     * 可以为查询发送多个结果
     */
    int MULTI_RESULTS = 1 << 17;

    /**
     * 可以为预处理语句发送多个结果
     */
    int CLIENT_PS_MULTI_RESULTS = 1 << 18;

    /**
     * 服务端：可以在初始握手包中发送额外数据，并支持可插拔身份验证协议
     * 客户端：使其支持插件认证
     */
    int PLUGIN_AUTH = 1 << 19;

    /**
     * Protocol::HandshakeResponse41 协议中，允许客户端的连接属性
     */
    int CLIENT_CONNECT_ATTRS = 1 <<20;

    /**
     * 在Protocol::HandshakeResponse41 协议中，可以识别身份验证响应数据的长度编码整数
     */
    int PLUGIN_AUTH_LENENC_CLIENT_DATA = 1 << 21;

    /**
     * 客户端可以处理过期的密码
     */
    int CLIENT_CAN_HANDLE_EXPIRED_PASSWORDS = 1 << 22;

    /**
     * 客户端会话跟踪
     */
    int CLIENT_SESSION_TRACK = 1 << 23;

    /**
     * 可以在发送文本结果后，再发送一个 ok 数据包
     */
    int CLIENT_DEPRECATE_EOF = 1 << 24;
}
