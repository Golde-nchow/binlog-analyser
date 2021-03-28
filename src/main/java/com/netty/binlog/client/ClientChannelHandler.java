package com.netty.binlog.client;

import com.netty.binlog.handler.*;
import com.netty.binlog.handler.fetcher.BinlogFetcher;
import com.netty.binlog.handler.fetcher.ServerIdFetcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author by chow
 * @Description 客户端 channel 处理器
 * @date 2021/3/6 下午3:31
 */
public class ClientChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("decoder", new BinlogDecoder());
        pipeline.addLast("encoder", new BinlogEncoder());
        pipeline.addLast("HandShakeHandler", new HandShakeHandler());
        pipeline.addLast("AuthenticationResultHandler", new AuthenticationResultHandler());
        pipeline.addLast("fetchBinlogInfoHandler", new FetcherHandler(new BinlogFetcher()));
        pipeline.addLast("fetchServerIdHandler", new FetcherHandler(new ServerIdFetcher()));
    }
}
