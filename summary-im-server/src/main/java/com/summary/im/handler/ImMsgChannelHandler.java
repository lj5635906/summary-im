package com.summary.im.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理通道
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
public class ImMsgChannelHandler extends ChannelInboundHandlerAdapter {

    private MsgHandlerAdapter msgHandlerAdapter;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("创建一个连接");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("关闭一个连接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ImMsgRequest request = (ImMsgRequest) msg;

        // 客户端消息处理
        ImMsgResponse adapter = msgHandlerAdapter.adapter(request, ctx);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("连接出现异常");
        super.exceptionCaught(ctx, cause);
    }
}
