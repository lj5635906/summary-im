package com.summary.im.server.netty.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.cache.ImChannelAttribute;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
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
public class ImServerMsgChannelHandler extends ChannelInboundHandlerAdapter {

    private final MsgHandlerAdapter msgHandlerAdapter;

    public ImServerMsgChannelHandler(MsgHandlerAdapter msgHandlerAdapter) {
        this.msgHandlerAdapter = msgHandlerAdapter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("创建一个连接");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("关闭一个连接");
        Long userId = ctx.channel().attr(ImChannelAttribute.USER_ID).get();
        if (null != userId) {
            UserCtxCacheManager.removeUserCtx(userId);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ImMsgRequest request = (ImMsgRequest) msg;

        // 客户端消息处理
        ImMsgResponse response = msgHandlerAdapter.adapter(request, ctx);

        // 向客户端做消息回执
        MsgAnswerHandler.answer(response, ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("连接出现异常");
        Long userId = ctx.channel().attr(ImChannelAttribute.USER_ID).get();
        if (null != userId) {
            UserCtxCacheManager.removeUserCtx(userId);
        }
        super.exceptionCaught(ctx, cause);
    }
}
