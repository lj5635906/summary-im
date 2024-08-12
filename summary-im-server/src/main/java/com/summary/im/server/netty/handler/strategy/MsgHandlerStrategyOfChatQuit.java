package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import com.summary.im.server.netty.handler.chat.PersonChatHandler;
import com.summary.im.server.netty.handler.chat.TeamChatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户端 退出某个聊天消息 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfChatQuit implements MsgHandlerStrategy {

    @Autowired
    private PersonChatHandler personChatHandler;
    @Autowired
    private TeamChatHandler teamChatHandler;

    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.chat_quit.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        // 清除当前用户具体的聊天
        UserCtxCacheManager.removeUserCurrentChat(request.getFromUserId());

        return null;
    }
}
