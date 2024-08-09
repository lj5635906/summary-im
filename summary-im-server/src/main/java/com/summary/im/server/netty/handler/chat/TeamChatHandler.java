package com.summary.im.server.netty.handler.chat;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.BodyType;
import com.summary.im.enums.MsgType;
import com.summary.im.server.entity.TeamMsgDO;
import com.summary.im.server.netty.handler.MsgAnswerHandler;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import com.summary.im.server.service.PersonMsgService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 群聊消息 处理逻辑
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Component
public class TeamChatHandler {

    @Autowired
    private PushTeamChatHandler pushTeamChatHandler;

    /**
     * 群聊天消息处理
     *
     * @param request 。
     * @return 服务端 群消息id
     */
    public Long handleTeamChat(ImMsgRequest request) {
        // 消息id
        long msgId = IdWorker.getId();

        TeamMsgDO teamMsg = TeamMsgDO.builder()
                .teamMsgId(msgId)
                .teamId(request.getTeamId())
                .fromUserId(request.getFromUserId())
                .msgType(request.getMsgType())
                .clientType(request.getFromClientType())
                .bodyType(request.getBodyType())
                .body(request.getBody())
                .voiceTime(request.getTime())
                .cancelFlag(false)
                .readFlag(false)
                .readNum(0)
                .msgIdClient(request.getMsgIdClient())
                .build();

        pushTeamChatHandler.pushTeamChat(teamMsg);

        return msgId;
    }


}
