package com.summary.im.server.netty;

import com.summary.im.server.netty.codec.tcp.MsgDecoder;
import com.summary.im.server.netty.codec.tcp.MsgEncoder;
import com.summary.im.server.netty.codec.ws.WebSocketMsgDecoder;
import com.summary.im.server.netty.codec.ws.WebSocketMsgEncoder;
import com.summary.im.server.netty.handler.ImServerSocketChannelHandler;
import com.summary.im.server.netty.handler.ImServerWebSocketChannelHandler;
import com.summary.im.server.netty.handler.MsgHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 动态确定链接
 *
 * @author jie.luo
 * @since 2024/8/12
 */
@Slf4j
public class SocketChooseHandler extends ByteToMessageDecoder {

    private final MsgHandlerAdapter msgHandlerAdapter;
    /**
     * 默认暗号长度为23
     */
    private static final int MAX_LENGTH = 23;
    /**
     * WebSocket握手的协议前缀
     */
    private static final String WEBSOCKET_PREFIX = "GET /";

    public SocketChooseHandler(MsgHandlerAdapter msgHandlerAdapter) {
        this.msgHandlerAdapter = msgHandlerAdapter;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (isWebSocket(byteBuf)) {
            ChannelPipeline pipeline = ctx.pipeline();
            pipeline.addLast(new HttpServerCodec());
            // Http消息组装
            pipeline.addLast("aggregator", new WebSocketFrameAggregator(65536));
            // WebSocket通信支持
            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
            pipeline.addLast("decoder", new WebSocketMsgDecoder());
            pipeline.addLast("encoder", new WebSocketMsgEncoder());
            pipeline.addLast("handler", new ImServerWebSocketChannelHandler(msgHandlerAdapter));
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws-message"));
            pipeline.remove(this);
        } else {
            ChannelPipeline pipeline = ctx.pipeline();
            pipeline.addLast("decoder", new MsgDecoder());
            pipeline.addLast("encoder", new MsgEncoder());
            pipeline.addLast("handler", new ImServerSocketChannelHandler(msgHandlerAdapter));
            pipeline.remove(this);
        }

        byteBuf.resetReaderIndex();
    }

    private static boolean isWebSocket(ByteBuf byteBuf) {
        int length = byteBuf.readableBytes();
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH;
        }

        // 标记读位置
        byteBuf.markReaderIndex();
        byte[] content = new byte[length];
        byteBuf.readBytes(content);
        String protocol = new String(content);

        // websocket 连接开始： GET /ws-message HTTP/1.1
        return protocol.startsWith(WEBSOCKET_PREFIX);
    }

}
