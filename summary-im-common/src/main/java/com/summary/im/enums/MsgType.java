package com.summary.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Getter
@AllArgsConstructor
public enum MsgType {
    // 心跳
    heartbeat(0),
    // 登录
    login(1),
    // 退出登录
    logout(2),
    // 聊天
    chat(3),
    // 聊天-服务端应答消息
    chat_answer(4),
    // 进入某个聊天
    chat_enter(5),
    // 退出某个聊天
    chat_quit(6),
    // 通知
    notice(7),
    ;

    private final int code;

    public static MsgType getMsgType(int code) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getCode() == code) {
                return msgType;
            }
        }
        return null;
    }
}
