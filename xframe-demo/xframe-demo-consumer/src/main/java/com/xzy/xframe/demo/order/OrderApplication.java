package com.xzy.xframe.demo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xzy on 2020/3/31  .
 */

@SpringBootApplication
@EnableFeignClients
//@ComponentScan(basePackages={"com.xzy.xframe.api"})
public class OrderApplication {

    public static void main(String[] args){
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
