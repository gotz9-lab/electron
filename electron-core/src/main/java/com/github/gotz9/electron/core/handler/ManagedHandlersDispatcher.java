package com.github.gotz9.electron.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ChannelHandler.Sharable
public abstract class ManagedHandlersDispatcher<T> extends SimpleChannelInboundHandler<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IHandlerManager handlerManager;

    private final ExecutorService executor;

    /**
     * @param manager  {@link IHandlerManager}, 用于获取消息对应的 manager.
     * @param executor {@link ExecutorService} 线程池, 用于执行 handler 的业务处理.
     */
    public ManagedHandlersDispatcher(IHandlerManager manager, ExecutorService executor) {
        this.handlerManager = manager;
        this.executor = executor != null
                ? executor
                : Executors.newFixedThreadPool(1);
    }

    public ManagedHandlersDispatcher(IHandlerManager manager) {
        this(manager, null);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        final int handlerId = getHandlerId(msg);
        IHandler handler = handlerManager.getHandler(handlerId);

        if (handler == null) {
            // 无法处理的消息
            logger.warn("not found handler for {}", handlerId);
            ctx.fireChannelRead(msg);
            return;
        }

        executor.execute(() -> {
            try {
                handler.handle(ctx.channel(), msg);
            } catch (Exception e) {
                logger.error("handler exception", e);
            }
        });
    }

    protected abstract int getHandlerId(T msg);

}
