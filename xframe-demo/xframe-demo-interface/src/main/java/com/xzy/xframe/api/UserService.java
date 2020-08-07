package com.xzy.xframe.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("xframe-demo-provider")
public interface UserService {

    @GetMapping("/getUser")
    String getUser(@RequestParam("userId") Long userId);

}