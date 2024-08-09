package com.summary.im.server.netty.handler;

import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端 向 客户端 应答消息处理器
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
public class MsgAnswerHandler {

    /**
     * 消息回执
     *
     * @param response {@link ImMsgResponse}
     * @param ctx      .
     */
    public static void answer(ImMsgResponse response, ChannelHandlerContext ctx) {
        if (null == response) {
            return;
        }

        if (ctx.channel().isActive()) {
            ctx.writeAndFlush(response).addListener(future -> {
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
