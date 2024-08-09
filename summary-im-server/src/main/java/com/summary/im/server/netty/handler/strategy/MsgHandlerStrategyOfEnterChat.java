package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.cache.ImChannelAttribute;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 客户端 进入某个聊天 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfEnterChat implements MsgHandlerStrategy {
    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.chat_enter.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        // 缓存当前用户为在线用户
        UserCtxCacheManager.putUserCtx(request.getFromUserId(), ctx);
        UserCtxCacheManager.putUserCurrentChat(request.getFromUserId(), request.getChatId());

        return null;
    }
}
