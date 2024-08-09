package com.summary.im.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summary.im.server.entity.ChatDO;
import com.summary.im.server.entity.PersonMsgDO;
import com.summary.im.server.mapper.ChatMapper;
import com.summary.im.server.mapper.PersonMsgMapper;
import com.summary.im.server.service.PersonMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jie.luo
 * @since 2024/8/7
 */
@Service
public class PersonMsgServiceImpl extends ServiceImpl<PersonMsgMapper, PersonMsgDO> implements PersonMsgService {

    @Autowired
    private PersonMsgMapper personMsgMapper;
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public Long savePersonMsg(Long chatId, PersonMsgDO personMsg) {

        Boolean readFlag = personMsg.getReadFlag();
        if (!readFlag) {
            // 离线消息
            // 聊天未读消息+1
            ChatDO chat = chatMapper.selectById(chatId);
            ChatDO modify = ChatDO.builder()
                    .chatId(chatId)
                    .build();
            if (chat.getFromUserId().equals(personMsg.getFromUserId())) {
                modify.setFromUnreadNum(chat.getFromUnreadNum() + 1);
            } else {
                modify.setToUnreadNum(chat.getToUnreadNum() + 1);
            }

            chatMapper.updateById(modify);
        }

        if (personMsg.getPersonMsgId() == null) {
            personMsg.setPersonMsgId(IdWorker.getId());
        }
        personMsgMapper.insert(personMsg);

        return personMsg.getPersonMsgId();
    }
}
