package com.summary.im.server.aop;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summary.im.enums.BodyType;
import com.summary.im.enums.ClientType;
import com.summary.im.enums.MsgType;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.TeamDO;
import com.summary.im.server.entity.TeamMsgDO;
import com.summary.im.server.mapper.ChatMapper;
import com.summary.im.server.mapper.TeamMapper;
import com.summary.im.server.mapper.TeamMsgMapper;
import com.summary.im.server.netty.handler.chat.PushTeamChatHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

/**
 * 创建群聊后，向群聊用户推送建群通知
 *
 * @author jie.luo
 * @since 2024/8/10
 */
@Slf4j
@Aspect
@Component
public class TeamChatAspect {

    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private PushTeamChatHandler pushTeamChatHandler;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamMsgMapper teamMsgMapper;

    /**
     * 切入点
     */
    @Pointcut("execution(* com.summary.im.server.service.impl.ChatServiceImpl.createTeamChat(..))")
    public void createTeamChatSuccessPointCut() {
    }

    @Around("createTeamChatSuccessPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        // 创建群聊 的参数
        Object[] args = joinPoint.getArgs();

        // 创建群聊 群主的 chatId
        Object result = joinPoint.proceed();

        if (null == result) {
            return null;
        }

        // 群主的 chatId
        Long chatId = (Long) result;

        // fromUserId
        Long fromUserId = (Long) args[0];

        TeamDO team = teamMapper.selectTeamByChatId(chatId);

        List<ChatDO> teamChats = chatMapper.selectTeamChatByChatId(chatId);

        String body = team.getTeamAdminName() + " 创建 " + team.getTeamName();

        // 创建群聊通知
        TeamMsgDO teamMsg = TeamMsgDO.builder()
                .teamMsgId(IdWorker.getId())
                .teamId(team.getTeamId())
                .fromUserId(fromUserId)
                .msgType(MsgType.chat.getCode())
                .clientType(ClientType.SERVER.getCode())
                .bodyType(BodyType.notification.getCode())
                .body(body)
                .voiceTime(0)
                .cancelFlag(false)
                .readFlag(false)
                .readNum(0)
                .msgIdClient("")
                .build();

        teamMsgMapper.insert(teamMsg);

        long createTime = team.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        for (ChatDO teamChat : teamChats) {
            pushTeamChatHandler.pushCreateTeamChatNotice(teamMsg, teamChat.getChatId(), teamChat.getTeamId(), fromUserId, teamChat.getFromUserId(), body, createTime);
        }


        return result;
    }
}
