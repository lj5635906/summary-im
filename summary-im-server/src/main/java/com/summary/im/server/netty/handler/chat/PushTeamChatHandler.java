package com.summary.im.server.netty.handler.chat;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.BodyType;
import com.summary.im.enums.ClientType;
import com.summary.im.enums.MsgType;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.TeamMemberMsgDO;
import com.summary.im.server.entity.TeamMsgDO;
import com.summary.im.server.netty.handler.MsgAnswerHandler;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import com.summary.im.server.service.ChatService;
import com.summary.im.server.service.TeamMemberMsgService;
import com.summary.im.server.service.TeamMsgService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/10
 */
@Component
public class PushTeamChatHandler {

    @Autowired
    private ChatService chatService;
    @Autowired
    private TeamMsgService teamMsgService;
    @Autowired
    private TeamMemberMsgService teamMemberMsgService;

    /**
     * 创建群聊消息通知
     *
     * @param chatId     聊天id
     * @param teamId     群id
     * @param fromUserId 建群用户id
     * @param toUserId   接收用户id
     */
    public void pushCreateTeamChatNotice(TeamMsgDO teamMsg, Long chatId, Long teamId, Long fromUserId, Long toUserId, String body, Long sendTime) {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        boolean readFlag = false;

        if (!fromUserId.equals(toUserId)) {
            ChannelHandlerContext toUserCtx = UserCtxCacheManager.getUserCtx(toUserId);
            if (toUserCtx != null) {
                // 检测成员在线
                Long userCurrentChat = UserCtxCacheManager.getUserCurrentChat(toUserId);
                if (userCurrentChat != null && userCurrentChat.equals(chatId)) {
                    // 对方正在当前聊天
                    // 消息已读标识 设置为已读
                    readFlag = true;
                }
                ImMsgResponse response = ImMsgResponse.builder()
                        .msgIdServer(teamMsg.getTeamMsgId())
                        .chatId(chatId)
                        .teamId(teamId)
                        .msgType(MsgType.notice.getCode())
                        .fromClientType(ClientType.SERVER.getCode())
                        .fromUserId(fromUserId)
                        .toUserId(toUserId)
                        .bodyType(BodyType.text.getCode())
                        .body(body)
                        .sendTime(sendTime)
                        .build();

                // 用户在线 推送消息
                MsgAnswerHandler.answer(response, toUserCtx);
            }
        } else {
            readFlag = true;
        }

        TeamMemberMsgDO teamMemberMsgDO = TeamMemberMsgDO.builder()
                .teamMemberMsgId(IdWorker.getId())
                .chatId(chatId)
                .teamId(teamId)
                .teamMsgId(teamMsg.getTeamMsgId())
                .fromUserId(teamMsg.getFromUserId())
                .toUserId(toUserId)
                .readFlag(readFlag)
                .readTime(now)
                .build();

        teamMemberMsgService.save(teamMemberMsgDO);
    }


    @Async
    public void pushTeamChat(TeamMsgDO teamMsg) {

        // 当前群消息的已读人数
        int readNum = 0;
        Long teamId = teamMsg.getTeamId();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

        // 团队成员
        List<ChatDO> chats = chatService.getTeamChatByTeamId(teamId);

        List<TeamMemberMsgDO> list = new ArrayList<>();
        for (ChatDO chat : chats) {
            boolean readFlag = false;
            if (teamMsg.getFromUserId().equals(chat.getFromUserId())) {
                readFlag = true;
            } else {
                // 检测成员是否在线
                ChannelHandlerContext toUserCtx = UserCtxCacheManager.getUserCtx(chat.getFromUserId());
                if (toUserCtx != null) {
                    // 检测成员在线
                    Long userCurrentChat = UserCtxCacheManager.getUserCurrentChat(chat.getFromUserId());
                    if (userCurrentChat != null && userCurrentChat.equals(chat.getChatId())) {
                        // 对方正在当前聊天
                        // 消息已读标识 设置为已读
                        readFlag = true;
                        readNum++;
                    }
                    ImMsgResponse response = ImMsgResponse.builder()
                            .msgIdServer(teamMsg.getTeamMsgId())
                            .chatId(chat.getChatId())
                            .teamId(teamId)
                            .msgType(teamMsg.getMsgType())
                            .fromClientType(ClientType.SERVER.getCode())
                            .fromUserId(teamMsg.getFromUserId())
                            .toUserId(chat.getFromUserId())
                            .bodyType(teamMsg.getBodyType())
                            .body(teamMsg.getBody())
                            .sendTime(now.toInstant(ZoneOffset.of("+8")).toEpochMilli())
                            .build();

                    // 用户在线 推送消息
                    MsgAnswerHandler.answer(response, toUserCtx);
                }
            }

            TeamMemberMsgDO teamMemberMsgDO = TeamMemberMsgDO.builder()
                    .teamMemberMsgId(IdWorker.getId())
                    .chatId(chat.getChatId())
                    .teamId(teamId)
                    .teamMsgId(teamMsg.getTeamMsgId())
                    .fromUserId(teamMsg.getFromUserId())
                    .toUserId(chat.getFromUserId())
                    .readFlag(readFlag)
                    .readTime(now)
                    .build();
            list.add(teamMemberMsgDO);
        }

        teamMsg.setReadNum(readNum);
        teamMsgService.save(teamMsg);
        teamMemberMsgService.saveBatch(list);
    }
}
