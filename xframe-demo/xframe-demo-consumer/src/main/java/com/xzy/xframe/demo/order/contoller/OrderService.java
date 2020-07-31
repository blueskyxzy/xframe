package com.xzy.xframe.demo.order.contoller;

import com.xzy.xframe.api.UserService;
import com.xzy.xframe.demo.order.openfeign.UserOpenFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.xzy.xframe.demo.order.strategy.RotationStrategy;

import java.net.URI;
import java.util.List;

/**
 * Created by xzy on 2020/3/31  .
 */

@RestController
public class OrderService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RotationStrategy rotationStrategy;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private UserOpenFeign userOpenFeign;

//    @Autowired
//    private UserService userService;

    @GetMapping("/orderUser")
    public Object getOrderUser(){
        // 通过服务名称从注册中心获取集群列表地址
        List<ServiceInstance> instances = discoveryClient.getInstances("xzynacos-user");
        // 负载均衡只获取一个服务实例来调  ,这里通过策略模式优化
        // 轮询策略
        ServiceInstance service = rotationStrategy.getSingleService(instances);
        // 服务集群实例通过实现本地RPC调用
        // 这里还有问题，如故障转移， 一个服务下线了需要重新获取健康的服务，不然用下线的服务调会出错，解决办法是循环中判断安全健康情况
        URI uri = service.getUri();
        String result = restTemplate.getForObject(uri + "/getUser", String.class);
        return result;
    }

    @GetMapping("/orderRibbon")
    public Object getOrderRibbon(){
        // Ribbon负载均衡  restTemplate需要@loadBlanced初始化
        String result = restTemplate.getForObject( "http://xzynacos-user/getUser?userId=1", String.class);
        return result;
    }

    @GetMapping("/orderBalanced")
    public Object getOrderBalanced(){
        // loadBalanceClient,     加上@LoadBalanced的uri解析的http:后面就是服务名而不是IP地址了，用restTemplate.getForObject调用会500
        ServiceInstance service = loadBalancerClient.choose("xzynacos-user");
        return service;
    }

    /**
     * 基于openFeign客户端调用服务
     */
    @GetMapping("/getUser")
    public Object getGetUser(){
        String result = userOpenFeign.getUser(1L);
        return result;
    }

/*
    */
/**
     * 基于openFeign客户端调用服务
     *//*

    @GetMapping("/getUser")
    public Object getGetUser(){
        String result = userService.getUser(1L);
        return result;
    }
*/


}
