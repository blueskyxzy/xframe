package com.xzy.xframe.provider.controller;

import com.frame.base.common.entity.BaseResp;
import com.xzy.xframe.entity.User;
import com.xzy.xframe.provider.service.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserDbService userDbService;

    @GetMapping("/userTest")
    public String getUserTest(){
        return "服务提供者 getUser:" + ",port:" + serverPort;
    }

    @GetMapping("/getNacosConfigTest")
    public String getNacosConfig(){
        return "getNacosConfig userHost:" + userId + ",userName:" + userName;
    }

    @GetMapping("/user")
    public BaseResp<User> getUserName(@RequestParam("id") Long id){
        User user = userDbService.getUser(id);
        return BaseResp.ok(user);
    }

    @PutMapping("/user")
    public BaseResp updateUser(@RequestBody User user){
        userDbService.updateUser(user);
        return BaseResp.ok();
    }

}
