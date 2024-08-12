package com.summary.im.client.cache;

import com.summary.im.enums.ClientType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端  ChannelHandlerContext  cache
 *
 * @author jie.luo
 * @since 2024/8/7
 */
@Slf4j
public class ClientCacheManager {

    /**
     * im 与 服务端 链接通道
     */
    private static ChannelHandlerContext ctx = null;
    /**
     * im user
     */
    private static Long IM_USER = null;

    /**
     * im client type
     */
    private static ClientType IM_CLIENT_TYPE = null;
    /**
     * im 用户 登录状态
     */
    private static Boolean isLogin = false;

    public static void cacheCtx(ChannelHandlerContext ctx) {
        log.debug("cache im ctx ...");
        ClientCacheManager.ctx = ctx;
        log.debug("cache im ctx success...");
    }

    public static ChannelHandlerContext getCtx() {
        if (null == ctx) {
            throw new RuntimeException("im ctx is null");
        }
        return ctx;
    }

    public static void clearCacheCtx() {
        ClientCacheManager.ctx = null;
    }

    public static void cacheImUser(Long userId) {
        ClientCacheManager.IM_USER = userId;
    }

    public static Long getImUser() {
        if (null == IM_USER) {
            throw new RuntimeException("im user is null");
        }
        return ClientCacheManager.IM_USER;
    }

    public static void clearImUser() {
        ClientCacheManager.IM_USER = null;
    }

    public static void cacheImClientType(ClientType clientType) {
        ClientCacheManager.IM_CLIENT_TYPE = clientType;
    }

    public static ClientType getImClientType() {
        if (null == IM_CLIENT_TYPE) {
            throw new RuntimeException("im clientType is null");
        }
        return ClientCacheManager.IM_CLIENT_TYPE;
    }

    public static void clearImClientType() {
        ClientCacheManager.IM_CLIENT_TYPE = null;
    }

    public static void updateLoginState() {
        ClientCacheManager.isLogin = !ClientCacheManager.isLogin;
    }

    public static Boolean getLoginState() {
        return ClientCacheManager.isLogin;
    }


}
