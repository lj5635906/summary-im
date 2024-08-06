package com.summary.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 事件类型
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Getter
@AllArgsConstructor
public enum EventType {
    // 点对点会话内消息
    person(0),
    // 群聊消息
    team(1);

    private final int code;

    public static EventType getByCode(int code) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getCode() == code) {
                return eventType;
            }
        }
        return null;
    }
}
