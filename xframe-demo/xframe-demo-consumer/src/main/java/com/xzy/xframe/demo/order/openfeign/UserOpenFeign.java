package com.xzy.xframe.demo.order.openfeign;

import com.frame.base.common.entity.BaseResp;
import com.xzy.xframe.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xzy on 2020/3/31  .
 */

@FeignClient("xframe-demo-provider")
public interface UserOpenFeign {

    @GetMapping("/getUser")
    String getUser(@RequestParam("userId") Long userId);

    @PutMapping("/user")
    BaseResp userPut(@RequestBody User user);

}
