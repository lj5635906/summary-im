package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 群成员消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("team_member_msg")
public class TeamMemberMsgDO extends BaseDO<TeamMemberMsgDO> {

    /*** 群成员消息id */
    @TableId(value = "team_member_msg_id", type = IdType.AUTO)
    private Long teamMemberMsgId;
    /*** 聊天id */
    private Long chatId;
    /*** 群id */
    private Long teamId;
    /*** 群消息id */
    private Long teamMsgId;
    /*** 用户id(消息发送人) */
    private Long fromUserId;
    /*** 用户id(消息接收人) */
    private Long toUserId;
    /*** 已读标识(0/false-否,1/true-是) */
    private Boolean readFlag;
    /*** 消息接受者阅读消息时间 */
    private LocalDateTime readTime;

    @Builder
    public TeamMemberMsgDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long teamMemberMsgId, Long chatId, Long teamId, Long teamMsgId, Long fromUserId, Long toUserId, Boolean readFlag, LocalDateTime readTime) {
        super(version, createTime, updateTime, deleteFlag);
        this.teamMemberMsgId = teamMemberMsgId;
        this.chatId = chatId;
        this.teamId = teamId;
        this.teamMsgId = teamMsgId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.readFlag = readFlag;
        this.readTime = readTime;
    }
}
