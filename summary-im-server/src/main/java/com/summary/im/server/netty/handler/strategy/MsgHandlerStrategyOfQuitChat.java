package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.cache.ImChannelAttribute;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 客户端 退出某个聊天 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfQuitChat implements MsgHandlerStrategy {
    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.chat_quit.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        // 缓存当前用户的聊天
        UserCtxCacheManager.removeUserCurrentChat(request.getFromUserId());

        return null;
    }
}
