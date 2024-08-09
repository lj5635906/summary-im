package com.summary.im.server.netty.handler.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 通道 缓存管理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
public class UserCtxCacheManager {

    /**
     * 用户-通道
     */
    private static final Map<Long, ChannelHandlerContext> USER_CTX_CACHE = new HashMap<>();
    /**
     * 用户-进入某个聊天
     */
    private static final Map<Long, Long> USER_CURRENT_CHAT = new HashMap<>();

    public static void putUserCurrentChat(Long userId, Long chatId) {
        USER_CURRENT_CHAT.put(userId, chatId);
    }

    public static Long getUserCurrentChat(Long userId) {
        return USER_CURRENT_CHAT.get(userId);
    }

    public static void removeUserCurrentChat(Long userId) {
        USER_CURRENT_CHAT.remove(userId);
    }

    public static void putUserCtx(Long userId, ChannelHandlerContext ctx) {
        USER_CTX_CACHE.put(userId, ctx);
    }

    public static ChannelHandlerContext getUserCtx(Long userId) {
        return USER_CTX_CACHE.get(userId);
    }

    public static void removeUserCtx(Long userId) {
        USER_CTX_CACHE.remove(userId);
    }
}
