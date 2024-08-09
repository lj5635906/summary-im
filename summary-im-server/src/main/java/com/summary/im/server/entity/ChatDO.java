package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 聊天
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("chat")
public class ChatDO extends BaseDO<ChatDO> {

    /*** 聊天id */
    @TableId(value = "chat_id", type = IdType.AUTO)
    private Long chatId;
    /*** 类型:点对点-0、群聊-1 */
    private Integer eventType;
    /*** 用户id(消息接收人) */
    private Long toUserId;
    /*** 用户id(消息发送人) */
    private Long fromUserId;
    /*** 群id */
    private Long teamId;
    /*** 未读消息数(对于消息接收人) */
    private Integer fromUnreadNum;
    /*** 未读消息数(对于消息发送人) */
    private Integer toUnreadNum;

    @Builder
    public ChatDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long chatId, Integer eventType, Long toUserId, Long fromUserId, Long teamId, Integer fromUnreadNum, Integer toUnreadNum) {
        super(version, createTime, updateTime, deleteFlag);
        this.chatId = chatId;
        this.eventType = eventType;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.teamId = teamId;
        this.fromUnreadNum = fromUnreadNum;
        this.toUnreadNum = toUnreadNum;
    }
}
