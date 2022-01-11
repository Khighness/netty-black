package top.parak.service;

import top.parak.annotation.server.RpcServer;

/**
 * @author KHighness
 */
@RpcServer
public class LBWServiceImpl implements HelloService{

    @Override
    public String sayHello(String name) {
        return "卢本伟说：你好"+name;
    }
}
