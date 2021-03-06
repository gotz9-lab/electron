package com.github.gotz9.electron.service.impl;

import com.github.gotz9.electron.service.AuthenticateService;
import com.github.gotz9.electron.message.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticateService implements AuthenticateService {

    public static final Logger logger = LoggerFactory.getLogger(DefaultAuthenticateService.class);

    @Override
    public boolean login(Login message) {
        logger.info("user {} login with token {}", message.getUuid(), message.getToken());
        return false;
    }

}
