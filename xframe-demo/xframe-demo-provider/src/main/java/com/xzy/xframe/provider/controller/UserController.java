package com.xzy.xframe.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author xzy
 * @date 2020/3/31
 */

@RestController
public class UserController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${user.id}")
    private String userId;

    @Value("${user.name}")
    private String userName;

    @GetMapping("/userTest")
    public String getUserTest(){
        return "服务提供者 getUser:" + ",port:" + serverPort;
    }

    @GetMapping("/getNacosConfigTest")
    public String getNacosConfig(){
        return "getNacosConfig userHost:" + userId + ",userName:" + userName;
    }

}
