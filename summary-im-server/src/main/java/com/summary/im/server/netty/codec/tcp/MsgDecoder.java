package com.summary.im.server.netty.codec.tcp;

import com.alibaba.fastjson.JSONObject;
import com.summary.im.base.ImMsgRequest;
import com.summary.im.encipher.SimpleEncipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息解码
 * 解码客户端发送过来的消息
 *
 * @author jie.luo
 * @since 2024/8/6
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

        // 消息转换为对象
        ImMsgRequest imMsg = JSONObject.parseObject(decipher, ImMsgRequest.class);

        log.debug("im server receive im client msg:{}", imMsg);
        list.add(imMsg);
    }
}
