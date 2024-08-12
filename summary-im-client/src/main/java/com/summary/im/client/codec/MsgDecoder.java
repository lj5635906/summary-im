package com.summary.im.client.codec;

import com.alibaba.fastjson.JSONObject;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.encipher.SimpleEncipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息解码
 * 解码服务端发送过来的消息
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class MsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf message, List<Object> list) throws Exception {

        // 数据长度
        int msgLen = message.readableBytes();
        byte[] bytes = new byte[msgLen];
        message.readBytes(bytes);

        // 消息解密
        byte[] decipher = new SimpleEncipher().decipher(bytes);

        log.debug("im client receive im server msg:{}", new String(decipher));
        // 消息转换为对象
        ImMsgResponse response = JSONObject.parseObject(decipher, ImMsgResponse.class);

        list.add(response);
    }
}
