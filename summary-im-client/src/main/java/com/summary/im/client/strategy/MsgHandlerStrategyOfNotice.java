package com.summary.im.client.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端 通知消息 回执处理逻辑
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgHandlerStrategyOfNotice implements MsgHandlerStrategy {

    @Override
    public ImMsgRequest doMsgHandler(ImMsgResponse response, ChannelHandlerContext ctx) {

        log.info("im client receive im server notice");
        // TODO 处理通知消息

        return null;
    }

}
