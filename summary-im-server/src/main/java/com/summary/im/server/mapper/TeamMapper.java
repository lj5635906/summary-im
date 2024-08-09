package com.summary.im.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.TeamDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 群
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Mapper
public interface TeamMapper extends BaseMapper<TeamDO> {

    /**
     * 获取该聊天 对应群信息
     *
     * @param chatId 。
     * @return 。
     */
    @Select("select * from team where team_id = (select team_id from chat where chat_id = #{chatId})")
    TeamDO selectTeamByChatId(@Param("chatId") Long chatId);
}
