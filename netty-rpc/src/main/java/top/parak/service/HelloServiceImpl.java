package top.parak.service;

import top.parak.annotation.server.RpcServer;

/**
 * @author KHighness
 */
@RpcServer
public class HelloServiceImpl implements HelloService{

    @Override
    public String sayHello(String name) {
        return "你好, " + name;
    }

}
