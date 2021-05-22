package com.github.gotz9.electron.handler.server;

import java.util.Objects;

import com.github.gotz9.electron.handler.IHandler;
import com.github.gotz9.electron.protocol.message.ClientMessage;

import io.netty.channel.Channel;

public abstract class ClientMessageHandler<T> implements IHandler<ClientMessage> {

    @Override
    public void handle(Channel ctx, ClientMessage message) {
        T body = Objects.requireNonNull(extractBody(message), "message body");

        handleMessage(ctx, body);
    }

    protected abstract T extractBody(ClientMessage message);

    protected abstract void handleMessage(Channel ctx, T body);

}
