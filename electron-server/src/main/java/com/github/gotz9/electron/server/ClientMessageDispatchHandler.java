package com.github.gotz9.electron.server;

import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.core.handler.ManagedHandlersDispatcher;
import com.github.gotz9.electron.message.ClientMessage;

import java.util.concurrent.ExecutorService;

public class ClientMessageDispatchHandler extends ManagedHandlersDispatcher<ClientMessage> {

    /**
     * @param manager  {@link IHandlerManager}, 用于获取消息对应的 manager.
     * @param executor {@link ExecutorService} 线程池, 用于执行 handler 的业务处理.
     */
    public ClientMessageDispatchHandler(IHandlerManager manager, ExecutorService executor) {
        super(manager, executor);
    }

    public ClientMessageDispatchHandler(IHandlerManager manager) {
        super(manager);
    }

    @Override
    protected int getHandlerId(ClientMessage msg) {
        return msg.getTypeValue();
    }

}
