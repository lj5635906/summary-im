package com.summary.im.client.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.client.ImClient;
import com.summary.im.client.cache.ClientCacheManager;
import com.summary.im.client.heartbeat.ClientCtxState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消息通道处理
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class ImClientChannelHandler extends ChannelInboundHandlerAdapter {

    private final MsgHandlerAdapter msgHandlerAdapter;

    private final ImClient imClient;

    public ImClientChannelHandler(MsgHandlerAdapter msgHandlerAdapter, ImClient imClient) {
        this.msgHandlerAdapter = msgHandlerAdapter;
        this.imClient = imClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("同 服务端 创建连接");
        ClientCacheManager.cacheCtx(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("channel inactive");
        // 异常 pipeline 上的链表 handler
        // 不在调用 channelInactive
        ctx.pipeline().remove(this);
        ctx.channel().close();

        reConnect(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ImMsgResponse response = (ImMsgResponse) msg;

        ImMsgRequest request = msgHandlerAdapter.adapter(response, ctx);

        MsgSendHandler.sendMsg(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ClientCtxState.setCurrentCtxState(ClientCtxState.State.EXCEPTION);
        log.warn("channel exception caught");

        // 异常 pipeline 上的链表 handler
        // 不在调用 channelInactive
        ctx.pipeline().remove(this);
        ctx.channel().close();

        reConnect(ctx);
    }

    /**
     * 重新连接 处理线程池
     */
    private static ScheduledExecutorService RE_CONNECT_SCHEDULED_EXECUTOR;
    /**
     * lock
     */
    private final Object lock = new Object();

    /**
     * 重连
     *
     * @param ctx .
     */
    public void reConnect(ChannelHandlerContext ctx) {

        if (ClientCtxState.getCurrentCtxState() == ClientCtxState.State.DISCONNECT_ACTIVE) {
            // 用户主动断开连接，不进行重连机制
            log.info("用户主动断开连接，不进行重连机制");
            return;
        }

        log.info("被动断开连接，5s 后自动重新连接");

        if (RE_CONNECT_SCHEDULED_EXECUTOR == null) {
            synchronized (lock) {
                RE_CONNECT_SCHEDULED_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
            }
        }

        RE_CONNECT_SCHEDULED_EXECUTOR.schedule(() -> {
            boolean isConnect = imClient.connect();
            if (isConnect) {
                RE_CONNECT_SCHEDULED_EXECUTOR.shutdown();
            } else {
                reConnect(ctx);
            }

        }, 3, TimeUnit.SECONDS);

    }

}
