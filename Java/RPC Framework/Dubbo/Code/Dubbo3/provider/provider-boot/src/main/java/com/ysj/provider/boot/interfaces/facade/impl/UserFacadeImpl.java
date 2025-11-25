package com.ysj.provider.boot.interfaces.facade.impl;


import com.ysj.provider.api.facade.UserFacade;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0")
public class UserFacadeImpl implements UserFacade {

    @Override
    public String getUser() {
        return "Hello,World";
    }
}
