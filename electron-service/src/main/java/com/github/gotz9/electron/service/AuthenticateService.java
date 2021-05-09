package com.github.gotz9.electron.service;

import com.github.gotz9.electron.protocol.message.Login;

public interface AuthenticateService {

    boolean login(Login message);

}
