package com.github.gotz9.electron.core.test;

import com.github.gotz9.electron.IHandlerManager;
import com.github.gotz9.electron.protocol.message.ClientMessage;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ElectronCoreTester {

    @Test
    public void handlerManagerTest() {
        IHandlerManager manager = new IHandlerManager("../electron-handler/target/classes");

        try {
            manager.loadHandler();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(manager.getHandler(ClientMessage.ClientMessageType.Login_VALUE));
    }

}
