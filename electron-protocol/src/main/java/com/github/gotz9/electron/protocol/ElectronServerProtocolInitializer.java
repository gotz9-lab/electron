package com.github.gotz9.electron.protocol;

import com.github.gotz9.electron.protocol.message.ClientMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * 读入 length based frame, 通过协议栈解析最终得到 ProtocolMessage 对象
 */
public class ElectronServerProtocolInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) {
        // 初始化协议栈
        ch.pipeline()
                .addLast("frame-decode", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, 0, Integer.BYTES))
                .addLast("frame-encode", new LengthFieldPrepender(Integer.BYTES));

        ch.pipeline()
                .addLast("protobuf-encoder", new ProtobufEncoder())
                .addLast("client-message-decoder", new ProtobufDecoder(ClientMessage.getDefaultInstance()));
    }

}