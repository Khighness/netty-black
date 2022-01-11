package top.parak.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡算法
 *
 * @author KHighness
 */
public interface LoadBalancer {

    Instance getInstance(List<Instance> list);

}
