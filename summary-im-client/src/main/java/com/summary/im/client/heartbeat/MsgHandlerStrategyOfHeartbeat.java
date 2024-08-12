package com.summary.im.client.heartbeat;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.client.strategy.MsgHandlerStrategy;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端 心跳消息 处理器
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public class MsgHandlerStrategyOfHeartbeat implements MsgHandlerStrategy {

    @Override
    public ImMsgRequest doMsgHandler(ImMsgResponse response, ChannelHandlerContext ctx) {

        ClientCtxState.setCurrentCtxState(ClientCtxState.State.NORMAL);

        return null;
    }

}