package com.xzy.xframe.demo.order.contoller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.frame.base.common.entity.BaseResp;
import com.xzy.xframe.demo.order.dao.OrderDao;
import com.xzy.xframe.demo.order.openfeign.UserOpenFeign;
import com.xzy.xframe.demo.order.service.TestService;
import com.xzy.xframe.entity.Order;
import com.xzy.xframe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private TestService testService;

    @Autowired
    private OrderDao orderDao;


//    @Autowired
//    private UserService userService;

    @GetMapping("/orderUser")
    public Object getOrderUser() {
        // 通过服务名称从注册中心获取集群列表地址
        List<ServiceInstance> instances = discoveryClient.getInstances("xframe-demo-provider");
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
    public Object getOrderRibbon() {
        // Ribbon负载均衡  restTemplate需要@loadBlanced初始化
        String result = restTemplate.getForObject("http://xframe-demo-provider/getUser?userId=1", String.class);
        return result;
    }

    @GetMapping("/orderBalanced")
    public Object getOrderBalanced() {
        // loadBalanceClient,     加上@LoadBalanced的uri解析的http:后面就是服务名而不是IP地址了，用restTemplate.getForObject调用会500
        ServiceInstance service = loadBalancerClient.choose("xframe-demo-provider");
        return service;
    }

    /**
     * 基于openFeign客户端调用服务
     */
    @GetMapping("/testTransactional")
    @Transactional(rollbackFor = Exception.class)
    public BaseResp test1() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setMobile("222");
        BaseResp result = userOpenFeign.userPut(user);
        Order order = new Order();
        order.setId(1L);
        order.setUserName("222");
        orderDao.updateByPrimaryKeySelective(order);
        if (true){
            throw new Exception("ex");
        }
        return BaseResp.ok();
    }

    /**
     * 基于openFeign客户端调用服务
     */
    @GetMapping("/test")
    public Object getGetUser() {
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

//
//    /**
//     * 秒杀接口
//     *
//     * @return
//     */
//    @RequestMapping("/seckill")
//    public String seckill(Long userId) {
//        Entry entry = null;
//        try {
//            // 对我们的userId的参数实现埋点
//            entry = SphU.entry(GET_SECKULL_RESOURCE_NAME, EntryType.IN,
//                    1, userId);
//            return "用户秒杀成功" + userId;
//        } catch (Exception e) {
//            return "用户秒杀失败,您访问的频率过多。";
//        } finally {
//            if (entry != null) {
//                entry.exit();
//            }
//
//        }
//
//    }
    /**
     * 定义我们的秒杀参数限流的规则
     */
    private static final String GET_SECKULL_RESOURCE_NAME = "GET_SECKULL";

    /**
     * 提前创建限流热词规则 基于qps
     */
    @RequestMapping("/seckill")
    @SentinelResource(value = "seckill", blockHandler = "seckillBlockHandler"
            , fallback = "seckillFallback")
    public String seckill(Long userId) {
        return "用户秒杀成功" + userId;
    }

    /**
     * 测试 @SentinelResource
     * 应用 @SentinelResource 注解，必须开启对应的切面，引入SentinelResourceAspect。
     */
    @RequestMapping("/testSR")
    @SentinelResource(value = "testSR", blockHandler = "seckillBlockHandler"
            , fallback = "seckillFallback")
    public String testSR(Long userId) {
        String testSR = testService.doSomeThing("testSR");
        return "用户秒杀成功" + userId + ":" + testSR;
    }

    @RequestMapping(value = "/testExHandle")
    @SentinelResource(value = "testExHandle", blockHandler = "seckillBlockHandler"
            , fallback = "seckillFallback")
    public String testExHandle(String str) {
//        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(WBListIpGlobalFilter.class);
//        Object wBListIpGlobalFilter = annotationConfigApplicationContext.getBean("wBListIpGlobalFilter");
        testService.handleEx();
        return "用户秒杀成功, testExHandle";
    }

    /**
     * blockHandler 限流出现异常执行的方法
     * fallback 服务熔断降级错误或者1.6版本以后接口出现业务逻辑异常执行
     */
    public String seckillBlockHandler(Long userId, BlockException e) {
        return "当前的访问次数过多，请稍后重试! BlockHandler";
    }

    public String seckillFallback(Long userId, Throwable e) {
        return "当前的访问次数过多，请稍后重试! Fallback";
    }

}
