package top.parak.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡随机算法
 *
 * @author KHighness
 */
public class RandomRule implements LoadBalancer{
    private final Random random=new Random();

    /**
     * 随机获取实例
     * @param list 服务器实例列表
     * @return 选中的服务器
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        return list.get(random.nextInt(list.size()));
    }

}
