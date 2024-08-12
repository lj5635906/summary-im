package com.summary.im.client.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端消息处理策略
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public interface MsgHandlerStrategy {

    /**
     * 处理 服务端 消息
     *
     * @param response {@link ImMsgRequest}
     * @param ctx      .
     * @return {@link ImMsgResponse}
     */
    ImMsgRequest doMsgHandler(ImMsgResponse response, ChannelHandlerContext ctx);
}
