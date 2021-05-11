package com.github.gotz9.electron;

import com.github.gotz9.electron.protocol.ElectronServerProtocolInitializer;
import com.github.gotz9.electron.protocol.message.ServerMessage;

public class ClientChannelInitializer extends ElectronServerProtocolInitializer {

    public ClientChannelInitializer() {
        super(ServerMessage.getDefaultInstance());
    }

}
