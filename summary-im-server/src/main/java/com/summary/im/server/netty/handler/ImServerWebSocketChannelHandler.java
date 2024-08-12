package com.summary.im.server.netty.handler;

import com.summary.im.base.ImMsgRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理通道
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
public class ImServerWebSocketChannelHandler extends ImServerSocketChannelHandler {

    public ImServerWebSocketChannelHandler(MsgHandlerAdapter msgHandlerAdapter) {
        super(msgHandlerAdapter);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest request) {
            // websocket 连接请求
            handHttpRequest(ctx, request);
        } else {
            if (msg instanceof ImMsgRequest) {
                super.channelRead(ctx, msg);
            } else {
                log.debug("other:{}", msg);
            }
        }

    }

    /**
     * 处理客户端向服务端发起 http 握手请求的业务
     * WebSocket在建立握手时，数据是通过HTTP传输的。但是建立之后，在真正传输时候是不需要HTTP协议的。
     * WebSocket 连接过程：
     * 首先，客户端发起http请求，经过3次握手后，建立起TCP连接；http请求里存放WebSocket支持的版本号等信息，如：Upgrade、Connection、WebSocket-Version等；
     * 然后，服务器收到客户端的握手请求后，同样采用HTTP协议回馈数据；
     * 最后，客户端收到连接成功的消息后，开始借助于TCP传输信道进行全双工通信。
     */
    private void handHttpRequest(ChannelHandlerContext ctx, HttpRequest request) {

        // 如果请求失败或者该请求不是客户端向服务端发起的 http 请求，则响应错误信息
        if (!request.decoderResult().isSuccess() || !("websocket".equals(request.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://" + request.headers().get("Host"), null, false);
        WebSocketServerHandshaker handShaker = wsFactory.newHandshaker(request);
        if (handShaker == null) {
            // 握手失败
            log.debug("握手失败....");
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 握手成功
            handShaker.handshake(ctx.channel(), request);
        }

    }

    /**
     * 服务端向客户端响应消息
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse response) {
        if (response.status().code() != 200) {
            //创建源缓冲区
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            //将源缓冲区的数据传送到此缓冲区
            response.content().writeBytes(byteBuf);
            //释放源缓冲区
            byteBuf.release();
        }
        //写入请求，服务端向客户端发送数据
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (response.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
