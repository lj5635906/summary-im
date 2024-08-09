package com.summary.im.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summary.im.server.entity.PersonMsgDO;

/**
 * 点对点消息 处理
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public interface PersonMsgService extends IService<PersonMsgDO> {

    /**
     * 保存点对点消息
     *
     * @param chatId    聊天id
     * @param personMsg 消息
     * @return 消息id
     */
    Long savePersonMsg(Long chatId, PersonMsgDO personMsg);

}
