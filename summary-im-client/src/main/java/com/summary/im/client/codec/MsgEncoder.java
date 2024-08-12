package com.summary.im.client.codec;

import com.alibaba.fastjson.JSON;
import com.summary.im.base.ImMsgRequest;
import com.summary.im.client.cache.ClientCacheManager;
import com.summary.im.encipher.SimpleEncipher;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息编码
 * 编码后发送服务端
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgEncoder extends MessageToMessageEncoder<ImMsgRequest> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImMsgRequest imMsg, List<Object> list) throws Exception {
        if (imMsg != null) {

            imMsg.setFromClientType(ClientCacheManager.getImClientType().getCode());
            imMsg.setFromUserId(ClientCacheManager.getImUser());

            log.debug("im client send to im server msg : {}", JSON.toJSONString(imMsg));
            // 消息对象序列化
            byte[] bytes = JSON.toJSONBytes(imMsg);

            // 消息加密
            byte[] encipher = new SimpleEncipher().encipher(bytes);

            // 消息发送
            list.add(Unpooled.buffer().writeBytes(encipher));
        }
    }
}
