package com.xzy.xframe.provider.controller;

import com.xzy.xframe.api.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author xzy
 * @date 2020/3/31
 */

@RestController
@RefreshScope
public class UserServiceImpl implements UserService {

    @Value("${server.port}")
    private String serverPort;

    @Value("${user.id}")
    private String userId;

    @Value("${user.name}")
    private String userName;

    @Override
    @GetMapping("/getUser")
    public String getUser(Long id){
        return "服务提供者 getUser:" + id + ",port:" + serverPort;
    }

    @GetMapping("/getNacosConfig")
    public String getNacosConfig(){
        return "getNacosConfig userHost:" + userId + ",userName:" + userName;
    }

}
