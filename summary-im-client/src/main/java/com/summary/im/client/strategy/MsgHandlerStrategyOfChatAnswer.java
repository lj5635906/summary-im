package com.summary.im.client.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天消息 回复 处理器
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgHandlerStrategyOfChatAnswer implements MsgHandlerStrategy {

    @Override
    public ImMsgRequest doMsgHandler(ImMsgResponse response, ChannelHandlerContext ctx) {

        log.debug("this is im server answer chat msg");

        return null;
    }

}