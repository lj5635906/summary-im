package com.summary.im.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 客户端 心跳消息 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfHeartbeat implements MsgHandlerStrategy {

    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.heartbeat.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMshHandler(ImMsgRequest request, ChannelHandlerContext ctx) {
        return ImMsgResponse.builder()
                .msgType(MsgType.heartbeat.getCode())
                .build();
    }
}
