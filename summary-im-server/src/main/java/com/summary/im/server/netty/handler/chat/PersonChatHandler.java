package com.summary.im.server.netty.handler.chat;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.server.entity.PersonMsgDO;
import com.summary.im.server.netty.handler.MsgAnswerHandler;
import com.summary.im.server.netty.handler.cache.UserCtxCacheManager;
import com.summary.im.server.service.PersonMsgService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 点对点消息 处理逻辑
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Component
public class PersonChatHandler {

    @Autowired
    private PersonMsgService personMsgService;

    public Long handlePersonChat(ImMsgRequest request) {
        // 消息id
        long msgId = IdWorker.getId();

        PersonMsgDO personMsg = PersonMsgDO.builder()
                .personMsgId(msgId)
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .clientType(request.getFromClientType())
                .bodyType(request.getBodyType())
                .body(request.getBody())
                .voiceTime(request.getTime())
                .cancelFlag(false)
                .readFlag(true)
                .msgIdClient(request.getMsgIdClient())
                .build();

        // 1. 查询对方在线状态
        ChannelHandlerContext toUserCtx = UserCtxCacheManager.getUserCtx(request.getToUserId());
        if (null != toUserCtx) {
            // 对方在线
            Long userCurrentChat = UserCtxCacheManager.getUserCurrentChat(request.getToUserId());
            if (userCurrentChat != null && userCurrentChat.equals(request.getChatId())) {
                // 对方正在当前聊天
                // 消息已读标识 设置为已读
                personMsg.setReadFlag(true);
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));

                // 构建消息传输对象
                ImMsgResponse response = ImMsgResponse.builder()
                        .chatId(request.getChatId())
                        .msgType(MsgType.chat.getCode())
                        .fromUserId(request.getFromUserId())
                        .toUserId(request.getToUserId())
                        .bodyType(request.getBodyType())
                        .body(request.getBody())
                        .msgIdClient(request.getMsgIdClient())
                        .msgIdServer(msgId)
                        .sendTime(now.toInstant(ZoneOffset.of("+8")).toEpochMilli())
                        .build();

                // 发送消息
                MsgAnswerHandler.answer(response, toUserCtx);
            }

        } else {
            // 对方离线
            // 消息已读标识 设置为未读
            personMsg.setReadFlag(false);
        }

        // 保存消息
        personMsgService.savePersonMsg(request.getChatId(), personMsg);

        return msgId;
    }

}
