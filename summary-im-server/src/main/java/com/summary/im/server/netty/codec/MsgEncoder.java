package com.summary.im.server.netty.codec;

import com.alibaba.fastjson.JSON;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.encipher.SimpleEncipher;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息编码
 * 编码后发送客户端
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
public class MsgEncoder extends MessageToMessageEncoder<ImMsgResponse> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImMsgResponse imMsg, List<Object> list) throws Exception {

        if (imMsg != null) {

            log.debug("im server send to im client msg : {}", imMsg);

            // 消息对象序列化
            byte[] bytes = JSON.toJSONBytes(imMsg);

            // 消息加密
            byte[] encipher = new SimpleEncipher().encipher(bytes);

            // 消息发送
            list.add(Unpooled.buffer().writeBytes(encipher));
        }

    }
}
