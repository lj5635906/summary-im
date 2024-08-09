package com.summary.im.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.PersonMsgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 点对点消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Mapper
public interface PersonMsgMapper extends BaseMapper<PersonMsgDO> {

}
