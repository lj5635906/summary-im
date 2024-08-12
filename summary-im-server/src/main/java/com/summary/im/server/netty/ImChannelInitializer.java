package com.summary.im.server.netty;

import com.summary.im.server.netty.handler.MsgHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 初始化
 *
 * @author jie.luo
 * @since 2024/8/12
 */
public class ImChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private final MsgHandlerAdapter msgHandlerAdapter;

    public ImChannelInitializer(MsgHandlerAdapter msgHandlerAdapter) {
        this.msgHandlerAdapter = msgHandlerAdapter;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast(new SocketChooseHandler(msgHandlerAdapter));
    }
}
