package com.summary.im.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summary.im.server.entity.ChatDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 聊天
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Mapper
public interface ChatMapper extends BaseMapper<ChatDO> {

    /**
     * 根据fromUserId,toUserId查询chatId
     *
     * @param fromUserId .
     * @param toUserId   .
     * @return chatId
     */
    @Select("select chat_id from chat where (from_user_id = #{fromUserId} and to_user_id = #{toUserId}) or (from_user_id = #{toUserId} and to_user_id = #{fromUserId})")
    Long selectChatIdByFromToUserId(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    /**
     * 获取该聊天 对应群 的所有聊天
     *
     * @param chatId 。
     * @return 。
     */
    List<ChatDO> selectTeamChatByChatId(@Param("chatId") Long chatId);

    /**
     * 获取该群 对应群 的所有聊天
     *
     * @param teamId 。
     * @return 。
     */
    List<ChatDO> selectTeamChatByTeamId(@Param("teamId") Long teamId);
}
