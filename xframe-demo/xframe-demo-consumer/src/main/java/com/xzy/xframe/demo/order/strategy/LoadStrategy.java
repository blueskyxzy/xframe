package com.xzy.xframe.demo.order.strategy;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xzy on 2020/3/31  .
 */

@Component
public interface LoadStrategy {

    ServiceInstance getSingleService(List<ServiceInstance> instances);
}
