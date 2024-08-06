package com.summary.im.handler.strategy;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.enums.MsgType;
import com.summary.im.handler.UserCtxCacheManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 客户端 退出登录消息 处理
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerStrategyOfLogout implements MsgHandlerStrategy {
    @Override
    public boolean support(ImMsgRequest request) {
        return MsgType.logout.getCode() == request.getMsgType();
    }

    @Override
    public ImMsgResponse doMshHandler(ImMsgRequest request, ChannelHandlerContext ctx) {

        // 清除 当前缓存的在线用户
        UserCtxCacheManager.removeUserCtx(request.getFromUserId());

        return ImMsgResponse.builder()
                .msgType(MsgType.logout.getCode())
                .build();
    }
}
