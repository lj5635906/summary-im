package com.summary.im.client.strategy;

import com.summary.im.client.heartbeat.MsgHandlerStrategyOfHeartbeat;
import com.summary.im.enums.MsgType;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端消息处理策略 容器
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public class MsgHandlerStrategyContainer {

    /**
     * 策略容器
     */
    private static final Map<MsgType, MsgHandlerStrategy> STRATEGY_CONTAINER = new HashMap<>();

    static {
        STRATEGY_CONTAINER.put(MsgType.heartbeat, new MsgHandlerStrategyOfHeartbeat());
        STRATEGY_CONTAINER.put(MsgType.login, new MsgHandlerStrategyOfLogin());
        STRATEGY_CONTAINER.put(MsgType.logout, new MsgHandlerStrategyOfLogout());
        STRATEGY_CONTAINER.put(MsgType.chat, new MsgHandlerStrategyOfChat());
        STRATEGY_CONTAINER.put(MsgType.chat_answer, new MsgHandlerStrategyOfChatAnswer());
        STRATEGY_CONTAINER.put(MsgType.chat_enter, new MsgHandlerStrategyOfChatEnter());
        STRATEGY_CONTAINER.put(MsgType.chat_quit, new MsgHandlerStrategyOfChatQuit());
        STRATEGY_CONTAINER.put(MsgType.notice, new MsgHandlerStrategyOfNotice());
    }

    public static MsgHandlerStrategy getMsgHandlerStrategy(MsgType msgType) {
        return STRATEGY_CONTAINER.get(msgType);
    }
}
