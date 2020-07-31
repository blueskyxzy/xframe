package com.xzy.xframe.demo.order.strategy;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xzy on 18/8/16  .
 */

@Component
public class RotationStrategy implements LoadStrategy{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceInstance getSingleService(List<ServiceInstance> instances) {
//        int rand = atomicInteger.incrementAndGet() % instances.size();
        int rand = incrementAndGetModulo(instances.size());
        return instances.get(rand);
    }

    // 下面是ribbon的代码，加入了自旋compareAndSet提供一定的并发性，相当于乐观锁
    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = (current + 1) % modulo;
        } while(!this.atomicInteger.compareAndSet(current, next));

        return next;
    }
}
