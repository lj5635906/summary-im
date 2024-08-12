package com.summary.im.server.netty.codec.ws;

import com.alibaba.fastjson.JSONObject;
import com.summary.im.base.ImMsgRequest;
import com.summary.im.encipher.SimpleEncipher;
import com.summary.im.enums.MsgType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/12
 */
@Slf4j
public class WebSocketMsgDecoder extends MessageToMessageDecoder<WebSocketFrame> implements WebSocketFrameDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {

        if (webSocketFrame instanceof CloseWebSocketFrame) {
            // 客户端主动关闭 websocket
            ImMsgRequest request = ImMsgRequest.builder()
                    .msgType(MsgType.logout.getCode())
                    .build();
            list.add(request);
        } else {
            ByteBuf message = webSocketFrame.content();
            // 数据长度
            int msgLen = message.readableBytes();
            byte[] bytes = new byte[msgLen];
            message.readBytes(bytes);
            // 消息解密
            byte[] decipher = new SimpleEncipher().decipher(bytes);

            // 消息转换为对象
            ImMsgRequest imMsg = JSONObject.parseObject(decipher, ImMsgRequest.class);

            log.debug("im websocket server receive im websocket client msg:{}", imMsg);
            list.add(imMsg);
        }

    }

}
