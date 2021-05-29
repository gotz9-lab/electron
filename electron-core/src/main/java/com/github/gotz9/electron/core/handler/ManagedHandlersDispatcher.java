package com.github.gotz9.electron.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public abstract class ManagedHandlersDispatcher<T> extends SimpleChannelInboundHandler<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IHandlerManager handlerManager;

    public ManagedHandlersDispatcher(IHandlerManager manager) {
        this.handlerManager = manager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        final int handlerId = getHandlerId(msg);
        IHandler handler = handlerManager.getHandler(handlerId);

        if (handler == null) {
            // 无法处理的消息
            logger.warn("unhandlable message: not found handler for {}", handlerId);
            return;
        }

        try {
            handler.handle(ctx.channel(), msg);
        } catch (Exception e) {
            logger.error("handler exception", e);
        }
    }

    protected abstract int getHandlerId(T msg);

}
