package com.summary.im.server.netty.handler;

import com.summary.im.base.ImMsgRequest;
import com.summary.im.base.ImMsgResponse;
import com.summary.im.server.netty.handler.strategy.MsgHandlerStrategy;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息处理适配器
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Component
public class MsgHandlerAdapter {

    @Autowired
    private List<MsgHandlerStrategy> msgHandlerStrategies;

    /**
     * 适配具体消息处理策略
     *
     * @param request {@link ImMsgRequest}
     * @param ctx     .
     * @return {@link ImMsgResponse}
     */
    public ImMsgResponse adapter(ImMsgRequest request, ChannelHandlerContext ctx) {

        for (MsgHandlerStrategy strategy : msgHandlerStrategies) {
            if (strategy.support(request)) {
                return strategy.doMsgHandler(request, ctx);
            }
        }

        return null;
    }
}
