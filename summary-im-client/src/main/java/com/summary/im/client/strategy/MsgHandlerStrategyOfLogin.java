package com.summary.im.client.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.client.cache.ClientCacheManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端 登录消息 回执处理逻辑
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgHandlerStrategyOfLogin implements MsgHandlerStrategy {

    @Override
    public ImMsgRequest doMsgHandler(ImMsgResponse response, ChannelHandlerContext ctx) {

        // 登录成功 更新登录状态
        ClientCacheManager.updateLoginState();

        log.info("im client login success");

        return null;
    }

}
