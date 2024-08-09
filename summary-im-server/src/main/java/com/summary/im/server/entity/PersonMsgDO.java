package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 点对点消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("person_msg")
public class PersonMsgDO extends BaseDO<PersonMsgDO> {

    /*** 点对点消息id */
    @TableId(value = "person_msg_id", type = IdType.AUTO)
    private Long personMsgId;
    /*** 用户id(消息发送人) */
    private Long fromUserId;
    /*** 用户id(消息接收人) */
    private Long toUserId;
    /*** 客户端类型 */
    private Integer clientType;
    /*** 消息体类型 */
    private Integer bodyType;
    /*** 消息体 */
    private String body;
    /*** 语音时长 */
    private Integer voiceTime;
    /*** 撤回标志(0/false-否,1/true-是) */
    private Boolean cancelFlag;
    /*** 已读标识(0/false-否,1/true-是) */
    private Boolean readFlag;
    /*** 客户端消息id */
    private String msgIdClient;

    @Builder
    public PersonMsgDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long personMsgId, Long fromUserId, Long toUserId, Integer clientType, Integer bodyType, String body, Integer voiceTime, Boolean cancelFlag, Boolean readFlag, String msgIdClient) {
        super(version, createTime, updateTime, deleteFlag);
        this.personMsgId = personMsgId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.clientType = clientType;
        this.bodyType = bodyType;
        this.body = body;
        this.voiceTime = voiceTime;
        this.cancelFlag = cancelFlag;
        this.readFlag = readFlag;
        this.msgIdClient = msgIdClient;
    }
}
