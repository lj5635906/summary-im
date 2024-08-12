package com.summary.im.server.netty.codec.ws;

import com.alibaba.fastjson.JSON;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.encipher.SimpleEncipher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/12
 */
@Slf4j
public class WebSocketMsgEncoder extends MessageToMessageEncoder<ImMsgResponse> implements WebSocketFrameEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImMsgResponse response, List<Object> list) throws Exception {
        if (response != null) {

            log.debug("im websocket server send to im websocket client msg : {}", response);

            // 消息对象序列化
            byte[] bytes = JSON.toJSONBytes(response);

            // 消息加密
            byte[] encipher = new SimpleEncipher().encipher(bytes);
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(encipher);
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(byteBuf);

            // 消息发送
            list.add(textWebSocketFrame);
        }
    }

}
