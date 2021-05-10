package com.github.gotz9.electron;

import com.github.gotz9.electron.configuration.ServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.github.gotz9.electron.configuration.ServerConfiguration.DEFAULT_HANDLER_BIN_PATH;

public class ApplicationLauncher {

    public static void main(String[] args) {
        ServerConfiguration configuration = loadConfiguration();

        IHandlerManager iHandlerManager = new IHandlerManager(configuration.getHandlerBinPath());
        try {
            iHandlerManager.loadHandler();
        } catch (Exception e) {
            // Handler 管理器初始化失败
            e.printStackTrace();
            return;
        }

        ServerBootstrap bootstrap = new ServerBootstrap()
                // Server 连接配置
                .group(new NioEventLoopGroup(configuration.getActor(), new NioEventLoopGroup(configuration.getWorker())))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024) // accept 等待队列长度
                // 用户连接配置
                .childHandler(new ServerChannelInitializer(iHandlerManager))
                .childOption(ChannelOption.SO_KEEPALIVE, true) // keep alive
                .childOption(ChannelOption.TCP_NODELAY, true) // 数据包无延迟发送
                ;

        bootstrap.bind(configuration.getPort()).syncUninterruptibly();

    }

    private static ServerConfiguration loadConfiguration() {
        String portStr = System.getProperty("el-port", "8080");
        String actorStr = System.getProperty("el-actor", "1");
        String workerStr = System.getProperty("el-worker", "1");
        String handlerBinPath = System.getProperty("el-handler-bin", DEFAULT_HANDLER_BIN_PATH);

        short prop = Short.parseShort(portStr);
        int actor = Integer.parseInt(actorStr);
        int worker = Integer.parseInt(workerStr);

        return new ServerConfiguration(prop, actor, worker, handlerBinPath);
    }

}
