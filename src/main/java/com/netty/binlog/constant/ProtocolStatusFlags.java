package com.netty.binlog.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author by chow
 * @Description 协议状态码
 * @date 2021/3/22 下午9:38
 */
public class ProtocolStatusFlags {

    /**
     * 使用 Map 作为缓存
     */
    private static final Map<Integer, String> STATUS_FLAGS = new LinkedHashMap<>();

    static {
        // 0x1：SERVER_STATUS_IN_TRANS
        STATUS_FLAGS.put(1, "服务器状态-服务器状态在事务中");
        // 0x2：SERVER_STATUS_AUTOCOMMIT
        STATUS_FLAGS.put(2, "服务器状态-已启用自动提交");
        // 0x8：SERVER_MORE_RESULTS_EXISTS
        STATUS_FLAGS.put(8, "服务器存在多个结果");
        // 0x10：SERVER_STATUS_NO_GOOD_INDEX_USED
        STATUS_FLAGS.put(16, "服务器状态-未使用良好索引");
        // 0x20：SERVER_STATUS_NO_INDEX_USED
        STATUS_FLAGS.put(32, "服务器状态-未使用索引");
        // 0x40：SERVER_STATUS_CURSOR_EXISTS
        STATUS_FLAGS.put(64, "服务器状态-游标存在");
        // 0x80：SERVER_STATUS_LAST_ROW_SENT
        STATUS_FLAGS.put(128, "服务器状态-最后一行已发送");
        // 0x100：SERVER_STATUS_DB_DROPPED
        STATUS_FLAGS.put(256, "服务器状态-数据库已删除");
        // 0x200：SERVER_STATUS_NO_BACKSLASH_ESCAPES
        STATUS_FLAGS.put(512, "服务器状态-反斜杠被解析");
        // 0x400：SERVER_STATUS_METADATA_CHANGED
        STATUS_FLAGS.put(1024, "服务器状态-元数据已被更改");
        // 0x800：SERVER_QUERY_WAS_SLOW
        STATUS_FLAGS.put(2048, "服务器发现慢查询");
        // 0x1000：SERVER_PS_OUT_PARAMS
        STATUS_FLAGS.put(4096, "服务器打印流输出参数");
        // 0x2000：SERVER_STATUS_IN_TRANS_READONLY
        STATUS_FLAGS.put(8192, "服务器状态-在事务中只读");
        // 0x4000：SERVER_SESSION_STATE_CHANGED
        STATUS_FLAGS.put(16384, "服务器会话状态已更改");
    }

    /**
     * 通过10进制状态码，获取状态信息
     * @param statusFlags 10进制状态码
     * @return 服务器状态信息
     */
    public static String getStatusFlagsDescription(int statusFlags) {
        String result = STATUS_FLAGS.get(statusFlags);
        return StringUtils.isEmpty(result) ? "" : result;
    }

}
