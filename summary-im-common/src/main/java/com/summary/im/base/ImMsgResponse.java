package com.summary.im.base;

import lombok.*;

/**
 * 服务端 向 客户端 发送的数据
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImMsgResponse extends ImMsg {

    /*** 服务端消息Id */
    private String msgIdServer;

    @Builder
    public ImMsgResponse(int msgType, Long teamId, Long fromUserId, Long toUserId, int fromClientType, String msgIdClient, int bodyType, String body, String msgIdServer) {
        super(msgType, teamId, fromUserId, toUserId, fromClientType, msgIdClient, bodyType, body);
        this.msgIdServer = msgIdServer;
    }
}
