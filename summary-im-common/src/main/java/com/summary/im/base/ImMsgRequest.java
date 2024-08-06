package com.summary.im.base;

import lombok.*;

/**
 * 客户端 向 服务端 发送的数据
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImMsgRequest extends ImMsg {

    /*** 语音时长（秒）*/
    private Integer time;

    @Builder
    public ImMsgRequest(int msgType, Long teamId, Long fromUserId, Long toUserId, int fromClientType, String msgIdClient, int bodyType, String body, Integer time) {
        super(msgType, teamId, fromUserId, toUserId, fromClientType, msgIdClient, bodyType, body);
        this.time = time;
    }
}
