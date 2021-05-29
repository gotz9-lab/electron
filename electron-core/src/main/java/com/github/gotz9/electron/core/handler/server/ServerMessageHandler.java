package com.github.gotz9.electron.core.handler.server;

import com.github.gotz9.electron.core.handler.IHandler;
import com.github.gotz9.electron.message.ServerMessage;
import io.netty.channel.Channel;

import java.util.Objects;

public abstract class ServerMessageHandler<T> implements IHandler<ServerMessage> {

    @Override
    public void handle(Channel ctx, ServerMessage message) {
        T body = Objects.requireNonNull(extractBody(message), "message body");

        handleMessage(ctx, body);
    }

    protected abstract T extractBody(ServerMessage message);

    protected abstract void handleMessage(Channel ctx, T body);

}
