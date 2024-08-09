package com.summary.im.server.netty.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端消息处理策略顶层接口
 *
 * @author jie.luo
 * @since 2024/8/6
 */
public interface MsgHandlerStrategy {

    /**
     * 验证具体策略是否支持
     *
     * @param request {@link ImMsgRequest}
     * @return .
     */
    boolean support(ImMsgRequest request);

    /**
     * 处理客户端消息
     *
     * @param request {@link ImMsgRequest}
     * @param ctx     .
     * @return {@link ImMsgResponse}
     */
    ImMsgResponse doMsgHandler(ImMsgRequest request, ChannelHandlerContext ctx);

}
