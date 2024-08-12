package com.summary.im.client.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.client.strategy.MsgHandlerStrategy;
import com.summary.im.client.strategy.MsgHandlerStrategyContainer;
import com.summary.im.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author jie.luo
 * @since 2024/8/7
 */
public class MsgHandlerAdapter {

    /**
     * 适配具体消息处理策略
     *
     * @param response {@link ImMsgResponse}
     * @param ctx      .
     * @return {@link ImMsgResponse}
     */
    public ImMsgRequest adapter(ImMsgResponse response, ChannelHandlerContext ctx) {

        MsgType msgType = MsgType.getMsgType(response.getMsgType());

        MsgHandlerStrategy strategy = MsgHandlerStrategyContainer.getMsgHandlerStrategy(msgType);

        return strategy.doMsgHandler(response, ctx);
    }

}
