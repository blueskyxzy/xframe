package com.xzy.xframe.demo.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.xzy.xframe.demo.order.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author: xzy
 * @create: 2020-08-05
 **/

@Service
public class TestServiceImpl implements TestService {

    @Override
    @SentinelResource(value = "doSomeThing")
    public String doSomeThing(String str) {
        return "doSomeThing" + str;
    }

    @Override
    public void handleEx() {
        throw new RuntimeException("发生异常");
    }
}
