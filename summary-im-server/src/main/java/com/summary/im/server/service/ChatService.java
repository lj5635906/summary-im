package com.summary.im.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summary.im.server.entity.ChatDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public interface ChatService extends IService<ChatDO> {

    /**
     * 创建 点对点 聊天
     *
     * @param fromUserId .
     * @param toUserId   .
     * @return chatId
     */
    Long createPersonChat(Long fromUserId, Long toUserId) throws Exception;

    /**
     * 创建群聊
     *
     * @param fromUserId 群创建人
     * @param toUserIds  群成员
     * @return chatId
     */
    Long createTeamChat(Long fromUserId, List<Long> toUserIds) throws Exception;

    /**
     * 获取该群 对应群 的所有聊天
     *
     * @param teamId 。
     * @return 。
     */
    List<ChatDO> getTeamChatByTeamId(@Param("teamId") Long teamId);
}
