package com.summary.im.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summary.im.enums.EventType;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.TeamDO;
import com.summary.im.server.entity.UserDO;
import com.summary.im.server.mapper.ChatMapper;
import com.summary.im.server.service.ChatService;
import com.summary.im.server.service.TeamService;
import com.summary.im.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, ChatDO> implements ChatService {

    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;

    @Override
    public Long createPersonChat(Long fromUserId, Long toUserId) throws Exception {
        if (fromUserId == null || toUserId == null || fromUserId.equals(toUserId)) {
            throw new Exception("聊天发起对象或聊天接收对象有异常");
        }

        Long chatId = chatMapper.selectChatIdByFromToUserId(fromUserId, toUserId);
        if (null != chatId) {
            return chatId;
        }

        ChatDO chat = new ChatDO();
        chat.setChatId(IdWorker.getId());
        chat.setEventType(EventType.person.getCode());
        chat.setFromUserId(fromUserId);
        chat.setToUserId(toUserId);
        chat.setTeamId(0L);
        chat.setFromUnreadNum(0);
        chat.setToUnreadNum(0);
        chatMapper.insert(chat);
        return chat.getChatId();
    }

    @Override
    @Transactional
    public Long createTeamChat(Long fromUserId, List<Long> toUserIds) throws Exception {

        if (fromUserId == null || toUserIds.isEmpty()) {
            throw new Exception("聊天发起对象或聊天接收对象有异常");
        }

        if (toUserIds.size() == 1) {
            // 创建 点对点 聊天
            return createPersonChat(fromUserId, toUserIds.get(0));
        }

        UserDO user = userService.getById(fromUserId);
        if (user == null) {
            throw new Exception("用户不存在");
        }

        TeamDO team = TeamDO.builder()
                .teamId(IdWorker.getId())
                .teamName("群聊" + IdWorker.getId())
                .teamAdmin(fromUserId)
                .teamAdminName(user.getUsername())
                .build();

        List<ChatDO> chats = new ArrayList<>(toUserIds.size() + 1);

        ChatDO chat = new ChatDO();
        chat.setChatId(IdWorker.getId());
        chat.setEventType(EventType.team.getCode());
        chat.setFromUserId(fromUserId);
        chat.setToUserId(0L);
        chat.setTeamId(team.getTeamId());
        chat.setFromUnreadNum(0);
        chat.setToUnreadNum(0);
        chats.add(chat);

        for (Long toUserId : toUserIds) {

            ChatDO chat1 = new ChatDO();
            chat1.setChatId(IdWorker.getId());
            chat1.setEventType(EventType.team.getCode());
            chat1.setFromUserId(toUserId);
            chat1.setToUserId(0L);
            chat1.setTeamId(team.getTeamId());
            chat1.setFromUnreadNum(0);
            chat1.setToUnreadNum(0);
            chats.add(chat1);
        }

        teamService.save(team);
        this.saveBatch(chats);

        return chat.getChatId();
    }

    @Override
    public List<ChatDO> getTeamChatByTeamId(Long teamId) {
        return chatMapper.selectTeamChatByTeamId(teamId);
    }

}
