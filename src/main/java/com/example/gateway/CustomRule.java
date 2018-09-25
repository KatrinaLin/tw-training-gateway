package com.example.gateway;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class CustomRule extends AbstractLoadBalancerRule {
    private Random random = new Random();

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        List<Server> reachableServers = loadBalancer.getReachableServers();
        int index = Math.abs(random.nextInt()) % reachableServers.size();
        return reachableServers.get(index);
    }
}
