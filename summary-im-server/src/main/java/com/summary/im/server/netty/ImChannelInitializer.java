package com.summary.im.server.netty;

import com.summary.im.server.netty.codec.tcp.MsgDecoder;
import com.summary.im.server.netty.codec.tcp.MsgEncoder;
import com.summary.im.server.netty.handler.ImServerSocketChannelHandler;
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
        // 添加数据编码解码
//        pipeline.addLast("decoder", new MsgDecoder());
//        pipeline.addLast("encoder", new MsgEncoder());
//        pipeline.addLast("handler", new ImServerSocketChannelHandler(msgHandlerAdapter));

        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast(new SocketChooseHandler(msgHandlerAdapter));
    }
}
