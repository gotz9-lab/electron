package com.github.gotz9.electron;

import com.github.gotz9.electron.configuration.ServerConfiguration;
import com.github.gotz9.electron.core.service.AuthenticateService;
import com.github.gotz9.electron.core.service.impl.DefaultAuthenticateService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) {
        ServerConfiguration configuration = loadConfiguration(args);

        AuthenticateService authenticateService = new DefaultAuthenticateService();

        ServerBootstrap bootstrap = new ServerBootstrap()
                // Server 连接配置
                .group(new NioEventLoopGroup(configuration.getActor(), new NioEventLoopGroup(configuration.getWorker())))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024) // accept 等待队列长度
                // 用户连接配置
                .childHandler(new ServerChannelInitializer(authenticateService))
                .childOption(ChannelOption.SO_KEEPALIVE, true) // keep alive
                .childOption(ChannelOption.TCP_NODELAY, true) // 数据包无延迟发送
                ;

        bootstrap.bind(configuration.getPort()).syncUninterruptibly();

    }

    private static ServerConfiguration loadConfiguration(String[] args) {
        String portStr = System.getProperty("el-port", "8080");
        String actorStr = System.getProperty("el-actor", "1");
        String workerStr = System.getProperty("el-worker", "-1");

        short prop = Short.parseShort(portStr);
        int actor = Integer.parseInt(actorStr);
        int worker = Integer.parseInt(workerStr);

        return new ServerConfiguration(prop, actor, worker);
    }

}
