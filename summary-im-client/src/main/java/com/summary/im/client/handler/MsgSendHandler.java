package com.summary.im.client.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.client.cache.ClientCacheManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息发送处理
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgSendHandler {

    public static void sendMsg(ImMsgRequest request) {
        if (null == request) {
            return;
        }

        ChannelHandlerContext ctx = ClientCacheManager.getCtx();
        if (ctx.channel().isActive()) {
            ctx.writeAndFlush(request).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("send msg success");
                } else {
                    log.error("send msg fail:{}", future.cause().getMessage());
                }
            });
        } else {
            log.error("channel is not active");
        }

    }

}
