package com.summary.im.client.heartbeat;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.client.handler.MsgSendHandler;
import com.summary.im.enums.MsgType;

/**
 * IM Client Heartbeat Check Thread
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public class ImClientHeartbeatCheckThread extends Thread {

    @Override
    public void run() {

        ImMsgRequest request = ImMsgRequest.builder()
                .msgType(MsgType.heartbeat.getCode())
                .build();

        MsgSendHandler.sendMsg(request);

    }
}
