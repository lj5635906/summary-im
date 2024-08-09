package com.summary.im.server.rest;

import com.summary.im.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/10
 */
@RestController
@RequestMapping("/chat")
public class ChatRest {

    @Autowired
    private ChatService chatService;

    /**
     * 创建 点对点 聊天
     *
     * @param fromUserId .
     * @param toUserId   .
     * @return chatId
     */
    @PostMapping("/createPersonChat")
    public Long createPersonChat(@RequestParam(name = "fromUserId") Long fromUserId, @RequestParam(name = "toUserId") Long toUserId) throws Exception {
        return chatService.createPersonChat(fromUserId, toUserId);
    }

    /**
     * 创建群聊
     *
     * @param fromUserId 群创建人
     * @param toUserIds  群成员
     * @return chatId
     */
    @PostMapping("/createTeamChat")
    public Long createTeamChat(@RequestParam(name = "fromUserId") Long fromUserId, @RequestParam(name = "toUserIds") List<Long> toUserIds) throws Exception {
        return chatService.createTeamChat(fromUserId, toUserIds);
    }

}
