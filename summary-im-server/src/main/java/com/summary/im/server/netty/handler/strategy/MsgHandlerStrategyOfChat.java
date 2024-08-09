package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.netty.handler.chat.PersonChatHandler;
import com.summary.im.server.netty.handler.chat.TeamChatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 客户端 聊天消息 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfChat implements MsgHandlerStrategy {

    @Autowired
    private PersonChatHandler personChatHandler;
    @Autowired
    private TeamChatHandler teamChatHandler;

    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.chat.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        long msgIdServer = 0;
        if (null != request.getTeamId()) {
            // 群聊
            msgIdServer = teamChatHandler.handleTeamChat(request);
        } else {
            // 点对点
            msgIdServer = personChatHandler.handlePersonChat(request);
        }

        return ImMsgResponse.builder()
                .msgIdClient(request.getMsgIdClient())
                .msgIdServer(msgIdServer)
                .chatId(request.getChatId())
                .teamId(request.getTeamId())
                .msgType(MsgType.chat_answer.getCode())
                .build();
    }
}
