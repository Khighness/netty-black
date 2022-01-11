package top.parak.demo;

import top.parak.manager.ClientProxy;
import top.parak.manager.RpcClientManager;
import top.parak.service.HelloService;
import top.parak.service.LBWServiceImpl;

/**
 * 客户端测试
 *
 * @author KHighness
 */
public class RpcClient {
    public static void main(String[] args) {
        RpcClientManager clientManager = new RpcClientManager();
        //创建代理对象
        HelloService service = new ClientProxy(clientManager).getProxyService(LBWServiceImpl.class);
        System.out.println(service.sayHello("KHighness"));
    }
}
