package com.summary.im.client.handler;

import com.summary.im.client.ImClient;
import com.summary.im.client.codec.MsgDecoder;
import com.summary.im.client.codec.MsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 初始化
 *
 * @author jie.luo
 * @since 2024/8/12
 */
public class ImClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private final MsgHandlerAdapter msgHandlerAdapter;

    private final ImClient imClient;

    public ImClientChannelInitializer(MsgHandlerAdapter msgHandlerAdapter, ImClient imClient) {
        this.msgHandlerAdapter = msgHandlerAdapter;
        this.imClient = imClient;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        pipeline.addLast(new ImClientChannelHandler(msgHandlerAdapter, imClient));
    }
}
