package com.netty.binlog.handler;

import com.netty.binlog.entity.pack.PackageData;
import com.netty.binlog.handler.fetcher.AbstractFetcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by chow
 * @Description 获取信息处理器
 * @date 2021/3/28 下午4:18
 */
public class FetcherHandler extends SimpleChannelInboundHandler<PackageData> {

    /**
     * 获取器
     */
    private AbstractFetcher fetcher;

    /**
     * 列数据
     */
    Map<String, String> fields = new LinkedHashMap<>();

    /**
     * 元数据数量
     */
    private AtomicInteger metaDataSequence = new AtomicInteger(1);

    public FetcherHandler(AbstractFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PackageData msg) throws Exception {
        fetcher.setCtx(ctx);

        ByteBuf content = msg.getContent();
        Integer packageSequenceId = msg.getHeader().getSequenceId();
        int sequenceId = metaDataSequence.get();

        if (packageSequenceId == 1) {
            // 如果是第一个包，那么存储的肯定是列数量
            int result = fetcher.getColumnCount(content);

            // 设置元数据数量
            metaDataSequence.compareAndSet(sequenceId, result + sequenceId);

        } else if (packageSequenceId <= sequenceId) {
            // 若少于元数据的数量，那么就一直读字段的属性（ Column Definition包 ），每次读完，包序列号就+1
            fetcher.getColumn(content, fields);
            System.out.println("======================== 第 "+ (packageSequenceId - 1) +" 个字段解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 1) {

            // EOF 数据包，是 OK_package、ERR_package、EOF_package 其中之一.
            // 代表元数据的结尾
            fetcher.getMetaDataEof(content);
            System.out.println("======================== 元数据 EOF_package 解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 2) {
            // 返回的结果：One or more Text Resultset Row
            // 若数据是 0xFB，则代表 NULL
            // 从内容开始，每个值都是一个 string<lenenc>
            fetcher.getColumnValue(content, sequenceId, fields);
            System.out.println("======================== 字段值解析完成 ========================");

        } else if (packageSequenceId == sequenceId + 3) {
            // 结果集的 EOF 结束包
            fetcher.getColumnValueEof(content);
            System.out.println("======================== 结果集 EOF_package 解析完成 ========================");

            // 移除 FetchBinlogHandler
            ctx.pipeline().remove(this);
        }
    }
}
