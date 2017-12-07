package com.fibbery.rpc.demo.server.service;

import com.fibbery.rpc.demo.api.IHelloService;

/**
 * @author fibbery
 * @date 17/11/30
 */
public class HelloServiceImpl implements IHelloService{

    @Override
    public String getHelloStr(String name) {
        return "Hello " + name;
    }
}
