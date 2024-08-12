package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.cache.ImChannelAttribute;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 客户端 退出登录消息 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfLogout implements MsgHandlerStrategy {
    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.logout.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        Long userId = ctx.channel().attr(ImChannelAttribute.USER_ID).get();
        if (null != userId) {
            UserCtxCacheManager.removeUserCtx(userId);
        }

        // 关闭退出登录的通道
        ctx.close();

        return null;
    }
}
