package com.summary.im.handler;

import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端 向 客户端 应答消息处理器
 *
 * @author jie.luo
 * @since 2024/8/6
 */
public class MsgAnswerHandler {

    /**
     * 消息回执
     *
     * @param response {@link ImMsgResponse}
     * @param ctx      .
     */
    public void answer(ImMsgResponse response, ChannelHandlerContext ctx) {
        if (null == response) {
            return;
        }
    }
}
