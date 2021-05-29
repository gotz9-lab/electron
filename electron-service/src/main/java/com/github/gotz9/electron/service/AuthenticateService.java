package com.github.gotz9.electron.service;

import com.github.gotz9.electron.message.Login;

public interface AuthenticateService {

    boolean login(Login message);

}
