package com.summary.im.server.netty;

import com.summary.im.server.config.ImProperties;
import com.summary.im.server.netty.handler.MsgHandlerAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

/**
 * @author jie.luo
 * @since 2024/8/9
 */
@Slf4j
@Configuration
public class ImServer {

    @Autowired
    private ImProperties imProperties;

    @Autowired
    private MsgHandlerAdapter msgHandlerAdapter;

    private ChannelFuture channelFuture;

    @Async
    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ImChannelInitializer(msgHandlerAdapter))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        channelFuture = serverBootstrap.bind(imProperties.getHost(), imProperties.getPort()).sync();
        log.debug("im server bind host【{}】 port【{}】", imProperties.getHost(), imProperties.getPort());
        log.debug("im server started");
        channelFuture.channel().closeFuture().sync();
    }

    @PreDestroy
    public void destroy() {
        if (channelFuture != null && channelFuture.channel() != null) {
            // 1. 通知在线用户[服务器即将关闭]
            // 2. 关闭现有的连接，关闭对应的线程
            if (channelFuture.channel().isOpen()) {
                channelFuture.channel().close();
            }
        }
        log.info("im server closed");
    }
}
