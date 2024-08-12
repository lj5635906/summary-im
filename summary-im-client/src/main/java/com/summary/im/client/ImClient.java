package com.summary.im.client;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.client.cache.ClientCacheManager;
import com.summary.im.client.handler.ImClientChannelInitializer;
import com.summary.im.client.handler.MsgHandlerAdapter;
import com.summary.im.client.handler.MsgSendHandler;
import com.summary.im.client.heartbeat.ClientCtxState;
import com.summary.im.client.heartbeat.ImClientHeartbeatCheckThread;
import com.summary.im.enums.ClientType;
import com.summary.im.enums.MsgType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * im client
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class ImClient {

    private final NioEventLoopGroup workGroup;
    private final Bootstrap bootstrap;
    private volatile ChannelFuture channelFuture;
    private final String host;
    private final int port;
    /**
     * 心跳check线程池
     */
    private ScheduledExecutorService heartbeatScheduledExecutor;
    /**
     * 重新连接 处理线程池
     */
    private ScheduledExecutorService autoReconnectScheduledExecutor;
    /**
     * 自动重连次数
     */
    private final AtomicInteger AUTO_RECONNECT_TIME = new AtomicInteger(0);

    private static volatile ImClient imClient;

    /**
     * 获取 Im client
     */
    public static ImClient instance(String host, int port, Long userId, ClientType clientType) {
        if (imClient == null) {
            synchronized (ImClient.class) {
                if (imClient == null) {
                    imClient = new ImClient(host, port, userId, clientType);
                }
            }
        }
        return imClient;
    }

    /**
     * 初始化 Im client
     *
     * @param host       host
     * @param port       port
     * @param userId     imUserId
     * @param clientType {@link ClientType}
     */
    private ImClient(String host, int port, Long userId, ClientType clientType) {
        this.host = host;
        this.port = port;
        workGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ImClientChannelInitializer(new MsgHandlerAdapter(), this));

        boolean isConnect = connect();
        if (isConnect) {
            login(userId, clientType);
        } else {
            log.error("im client connect host【{}】 port【{}】 failed , 5s 后自动重新连接", host, port);
            // 自动重连
            autoReConnect(userId, clientType);
        }
    }

    /**
     * 自动重连
     */
    private void autoReConnect(Long userId, ClientType clientType) {
        if (autoReconnectScheduledExecutor == null) {
            autoReconnectScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        autoReconnectScheduledExecutor.schedule(() -> {
            log.error("im client connect host【{}】 port【{}】 第[{}]次重试", host, port, AUTO_RECONNECT_TIME.incrementAndGet());
            if (this.connect()) {
                login(userId, clientType);
                autoReconnectScheduledExecutor.shutdown();
            } else {
                autoReConnect(userId, clientType);
            }
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * 连接 Im server
     */
    public boolean connect() {

        channelFuture = bootstrap.connect(host, port);
        // 等待连接完成
        channelFuture.awaitUninterruptibly();

        if (channelFuture.isSuccess()) {
            log.debug("im client connect host【{}】 port【{}】", host, port);
            log.debug("im client started");
            // 开启心跳机制
            startHeartbeatThread();
            return true;
        }

        return false;
    }


    /**
     * 关闭连接
     */
    private void close() {
        // 心跳线程
        if (heartbeatScheduledExecutor != null) {
            heartbeatScheduledExecutor.shutdown();
        }
        if (channelFuture != null && channelFuture.channel() != null) {
            if (channelFuture.channel().isOpen()) {
                channelFuture.channel().close();
            }
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }

        log.info("im client closed");
    }

    /**
     * 登录
     *
     * @param userId     im user
     * @param clientType im clientType {@link ClientType}
     */
    private void login(Long userId, ClientType clientType) {

        ClientCacheManager.cacheImUser(userId);
        ClientCacheManager.cacheImClientType(clientType);

        ImMsgRequest request = ImMsgRequest.builder()
                .msgType(MsgType.login.getCode())
                .build();

        MsgSendHandler.sendMsg(request);

    }

    /**
     * 进入某个聊天
     *
     * @param chatId 聊天id
     */
    public void enterChat(Long chatId) {

        ImMsgRequest request = ImMsgRequest.builder()
                .msgType(MsgType.chat_enter.getCode())
                .chatId(chatId)
                .build();

        MsgSendHandler.sendMsg(request);
    }

    /**
     * 退出某个聊天
     *
     * @param chatId 聊天id
     */
    public void quitChat(Long chatId) {

        ImMsgRequest request = ImMsgRequest.builder()
                .msgType(MsgType.chat_quit.getCode())
                .chatId(chatId)
                .build();

        MsgSendHandler.sendMsg(request);
    }

    /**
     * im 退出登录
     */
    public void logout() {

        ImMsgRequest request = ImMsgRequest.builder()
                .msgType(MsgType.logout.getCode())
                .build();
        MsgSendHandler.sendMsg(request);

        ClientCacheManager.clearImUser();
        ClientCacheManager.clearImClientType();
        ClientCacheManager.clearCacheCtx();
        ClientCtxState.setCurrentCtxState(ClientCtxState.State.DISCONNECT_ACTIVE);

        close();
    }

    /**
     * 开启一个线程，每隔5秒 与服务端 进行一次心跳通信
     */
    private void startHeartbeatThread() {
        ImClientHeartbeatCheckThread heartbeatCheck = new ImClientHeartbeatCheckThread();
        heartbeatCheck.setDaemon(true);
        heartbeatScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        // 每隔5秒检查 进行一次心跳通信
        heartbeatScheduledExecutor.scheduleWithFixedDelay(heartbeatCheck, 1, 5, TimeUnit.SECONDS);
    }

}
