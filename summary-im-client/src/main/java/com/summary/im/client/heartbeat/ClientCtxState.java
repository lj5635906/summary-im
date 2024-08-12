package com.summary.im.client.heartbeat;

/**
 * 心跳检测链接状态
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public class ClientCtxState {

    public enum State {
        // 链接正常：登录正常
        NORMAL,
        // 链接断开-用户主动断开连接
        DISCONNECT_ACTIVE,
        // 链接断开-被动
        DISCONNECT,
        // 链接异常
        EXCEPTION
    }

    /**
     * 默认链接状态为：链接断开
     */
    private static State CURRENT_CTX_STATE = State.DISCONNECT;

    public static State getCurrentCtxState() {
        return CURRENT_CTX_STATE;
    }

    public static void setCurrentCtxState(State currentCtxState) {
        CURRENT_CTX_STATE = currentCtxState;
    }

}
