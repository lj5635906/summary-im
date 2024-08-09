package com.summary.im.server.netty.handler.cache;

import io.netty.util.AttributeKey;

/**
 * 通道属性
 *
 * @author jie.luo
 * @since 2024/8/7
 */
public class ImChannelAttribute {

    /**
     * 通道用户标识
     */
    public static final AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

}
