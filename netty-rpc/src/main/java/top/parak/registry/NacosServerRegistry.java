package top.parak.registry;

import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * nacos注册
 *
 * @author KHighness
 */
@Slf4j
public class NacosServerRegistry implements ServerRegistry {

    /**
     * 服务注册
     * @param serviceName        服务名称
     * @param inetSocketAddress  服务地址
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtils.registerServer(serviceName,inetSocketAddress);
            log.debug("nacos注册服务：{}", serviceName);
        } catch (NacosException e) {
            throw new RuntimeException("注册Nacos出现异常");
        }
    }

    /**
     * 获取服务
     * @param serviceName 服务名称
     * @return 服务地址
     */
    @Override
    public InetSocketAddress getService(String serviceName) {
        return null;
    }
}
