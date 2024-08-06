package com.summary.im.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IM 消息体
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImMsg {
    /*** 消息类型 */
    private int msgType;
    /*** 群聊消息的群id */
    private Long teamId;
    /*** 消息发送者用户Id */
    private Long fromUserId;
    /*** 消息接收者用户Id */
    private Long toUserId;
    /*** 发送客户端类型 */
    private int fromClientType;
    /*** 客户端消息Id */
    private String msgIdClient;
    /*** 消息体类型 */
    private int bodyType;
    /*** 消息体 */
    private String body;

}
