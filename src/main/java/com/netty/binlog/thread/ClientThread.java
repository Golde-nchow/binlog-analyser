package com.netty.binlog.thread;

import com.netty.binlog.client.ClientChannelHandler;
import com.netty.binlog.constant.ClientConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author by chow
 * @Description 客户端线程类
 * @date 2021/3/6 下午3:08
 */
public class ClientThread implements Runnable {

    @Override
    public void run() {
        // 线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientChannelHandler());

            ChannelFuture channelFuture = clientBootstrap.connect(ClientConstant.IP, ClientConstant.PORT).sync();

            System.out.println("MySQL 连接成功");

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println("MySQL 连接失败");
            System.out.println(e.getMessage());

        } finally {
            group.shutdownGracefully();
        }
    }
}
