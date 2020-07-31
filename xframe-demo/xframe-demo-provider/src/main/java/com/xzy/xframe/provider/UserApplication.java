package com.xzy.xframe.provider;

import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

/**
 * Created by xzy on 2020/3/31  .
 */

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args){
        SpringApplication.run(UserApplication.class);
    }

}