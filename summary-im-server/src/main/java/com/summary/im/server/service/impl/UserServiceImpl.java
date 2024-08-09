package com.summary.im.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summary.im.server.entity.UserDO;
import com.summary.im.server.mapper.UserMapper;
import com.summary.im.server.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author jie.luo
 * @since 2024/8/10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
