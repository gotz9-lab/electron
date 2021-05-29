package com.github.gotz9.electron.server;

import com.github.gotz9.electron.ServiceContextManager;
import com.github.gotz9.electron.core.compile.ElectronCompiler;
import com.github.gotz9.electron.core.handler.IHandlerManager;
import com.github.gotz9.electron.server.configuration.ServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.github.gotz9.electron.server.configuration.ServerConfiguration.DEFAULT_HANDLER_BIN_PATH;
import static com.github.gotz9.electron.server.configuration.ServerConfiguration.DEFAULT_HANDLER_SRC_PATH;


public class ApplicationLauncher {

    public static final Logger LOG = LoggerFactory.getLogger(ApplicationLauncher.class);

    public static void main(String[] args) {
        ServerConfiguration configuration = loadConfiguration();

        try {
            ServiceContextManager.CONTEXT = new AnnotationConfigApplicationContext(configuration.getRegisteredConfiguration());
        } catch (Exception e) {
            LOG.error("service context init failed", e);
            return;
        }

        try {
            ElectronCompiler compiler = new ElectronCompiler(configuration.getHandlerSrcPath(), configuration.getHandlerBinPath());
            compiler.compileAll();
        } catch (Exception e) {
            LOG.error("handler compile failed", e);
            return;
        }

        IHandlerManager iHandlerManager = new IHandlerManager(configuration.getHandlerBinPath());
        try {
            iHandlerManager.loadHandler();
        } catch (Exception e) {
            LOG.error("handler manager failed", e);
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
        String handlerSrcPath = System.getProperty("el-handler-src", DEFAULT_HANDLER_SRC_PATH);
        String configurationClasses = System.getProperty("el-context-configurations", "");

        short prop = Short.parseShort(portStr);
        int actor = Integer.parseInt(actorStr);
        int worker = Integer.parseInt(workerStr);

        String[] names = configurationClasses.split(",");

        Class<?>[] classes = new Class<?>[names.length];
        try {
            for (int i = 0, namesLength = names.length; i < namesLength; i++) {
                Class<?> clazz = Class.forName(names[i]);
                classes[i] = clazz;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("el-context-configurations", e);
        }

        return new ServerConfiguration(prop, actor, worker, handlerSrcPath, handlerBinPath, classes);
    }

}
