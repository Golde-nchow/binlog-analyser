package com.netty.binlog.event;

import com.netty.binlog.constant.EventType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author by chow
 * @Description 事件解析工厂
 * @date 2021/3/30 下午10:37
 */
public class EventParserFactory {

    private static Map<Integer, IEventParser> parserMap = new LinkedHashMap<>();

    static {
        parserMap.put(EventType.FORMAT_DESCRIPTION_EVENT.ordinal(), new FormatDescriptionEventParser());
        parserMap.put(EventType.ROTATE_EVENT.ordinal(), new RotateEventParser());
        parserMap.put(EventType.QUERY_EVENT.ordinal(), new QueryEventParser());
    }

    /**
     * 获取事件解析器
     * @param eventType 事件类型
     * @return 对应事件解析器
     */
    public static IEventParser getEventParser(Integer eventType) {
        return parserMap.get(eventType);
    }

}
