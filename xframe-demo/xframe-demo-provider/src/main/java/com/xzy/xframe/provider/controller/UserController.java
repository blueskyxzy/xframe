package com.xzy.xframe.provider.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author xzy
 * @date 2020/3/31
 */

@RestController
public class UserController {

    @NacosInjected
    private NamingService namingService;

    @Value("${server.port}")
    private String serverPort;

    @Value("${user.id}")
    private String userId;

    @Value("${user.name}")
    private String userName;

    @RequestMapping(value = "/userTest", method = RequestMethod.GET)
    public String getUserTest(Long userId){
        return "服务提供者 getUser:" + userId + ",port:" + serverPort;
    }

    @GetMapping("/getNacosConfigTest")
    public String getNacosConfig(){
        return "getNacosConfig userHost:" + userId + ",userName:" + userName;
    }

    @GetMapping("/getInstanceTest")
    @ResponseBody
    public List<Instance> getInstance(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

}
