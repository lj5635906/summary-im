package com.summary.im.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summary.im.server.entity.TeamDO;
import com.summary.im.server.mapper.TeamMapper;
import com.summary.im.server.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * @author jie.luo
 * @since 2024/8/10
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, TeamDO> implements TeamService {

}
