package com.summary.im.handler;

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
     * 群-群成员
     */
    private static final Map<Long, List<Long>> TEAM_MEMBER_CACHE = new HashMap<>();
    /**
     * 用户-通道
     */
    private static final Map<Long, ChannelHandlerContext> USER_CTX_CACHE = new HashMap<>();

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
