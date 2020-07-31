package com.xzy.xframe.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author xzy
 * @date 2020/3/31
 */

@RestController
public class UserController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/userTest", method = RequestMethod.GET)
    public String getUserTest(Long userId){
        return "服务提供者 getUser:" + userId + ",port:" + serverPort;
    }

}
