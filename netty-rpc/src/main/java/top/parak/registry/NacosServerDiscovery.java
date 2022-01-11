package top.parak.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import top.parak.loadbalancer.RoundRobinRule;
import top.parak.loadbalancer.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现接口
 *
 * @author KHighness
 */
@Slf4j
public class NacosServerDiscovery implements ServerDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServerDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer == null ? new RoundRobinRule() : loadBalancer;
    }

    /**
     * 根据服务名找到服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    @Override
    public InetSocketAddress getService(String serviceName) throws NacosException {
        List<Instance> instanceList = NacosUtils.getAllInstance(serviceName);
        log.debug("发现服务：{}", serviceName);
        if (instanceList.size() == 0) {
            throw new RuntimeException("找不到对应服务");
        }
        Instance instance = loadBalancer.getInstance(instanceList);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }

}
