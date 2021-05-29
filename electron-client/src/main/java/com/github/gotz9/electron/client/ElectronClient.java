package com.github.gotz9.electron.client;

import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.message.ClientMessage;
import com.github.gotz9.electron.message.Login;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.UUID;

public class ElectronClient {

    private static final Logger LOG = LoggerFactory.getLogger(ElectronClient.class);

    private final Bootstrap bootstrap;

    private final SocketAddress address;

    private volatile Channel channel;

    public ElectronClient(SocketAddress address, IHandlerManager manager) {
        this.address = address;
        this.bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer(manager));
    }

    private Channel fetchChannel() {
        if (channel == null || !channel.isActive()) {
            synchronized (this) {
                if (channel == null || !channel.isActive()) {
                    final ChannelFuture future;
                    try {
                        future = bootstrap.connect(address);
                        this.channel = future.sync().channel();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return channel;
    }

    public void login() {
        sendMessage(ClientMessage.newBuilder()
                .setType(ClientMessage.ClientMessageType.Login)
                .setLogin(Login.newBuilder()
                        .setToken(UUID.randomUUID().toString())
                        .setUuid(1)
                        .build())
                .build());
    }

    public void sendMessage(ClientMessage message) {
        fetchChannel().writeAndFlush(message);
    }

    public void close() {
        fetchChannel().closeFuture().syncUninterruptibly();
    }

}
