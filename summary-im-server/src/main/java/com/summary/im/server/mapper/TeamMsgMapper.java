package com.summary.im.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summary.im.server.entity.TeamDO;
import com.summary.im.server.entity.TeamMsgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 群消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Mapper
public interface TeamMsgMapper extends BaseMapper<TeamMsgDO> {

}
