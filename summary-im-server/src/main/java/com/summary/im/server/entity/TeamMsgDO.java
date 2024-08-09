package com.summary.im.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.summary.im.server.entity.base.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 群消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("team_msg")
public class TeamMsgDO extends BaseDO<TeamMsgDO> {

    /*** 群消息id */
    @TableId(value = "team_msg_id", type = IdType.AUTO)
    private Long teamMsgId;
    /*** 群id */
    private Long teamId;
    /*** 用户id(消息发送人) */
    private Long fromUserId;
    /*** 客户端类型 */
    private Integer clientType;
    /*** 消息类型 */
    private Integer msgType;
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
    /*** 消息已读人数 */
    private Integer readNum;
    /*** 客户端消息id */
    private String msgIdClient;

    @Builder
    public TeamMsgDO(Integer version, LocalDateTime createTime, LocalDateTime updateTime, Boolean deleteFlag, Long teamMsgId, Long teamId, Long fromUserId, Integer clientType, Integer msgType, Integer bodyType, String body, Integer voiceTime, Boolean cancelFlag, Boolean readFlag, Integer readNum, String msgIdClient) {
        super(version, createTime, updateTime, deleteFlag);
        this.teamMsgId = teamMsgId;
        this.teamId = teamId;
        this.fromUserId = fromUserId;
        this.clientType = clientType;
        this.msgType = msgType;
        this.bodyType = bodyType;
        this.body = body;
        this.voiceTime = voiceTime;
        this.cancelFlag = cancelFlag;
        this.readFlag = readFlag;
        this.readNum = readNum;
        this.msgIdClient = msgIdClient;
    }
}
