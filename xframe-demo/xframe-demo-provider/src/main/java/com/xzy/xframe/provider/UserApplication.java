package com.xzy.xframe.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Created by xzy on 2020/3/31  .
 */

@RefreshScope
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args){
        SpringApplication.run(UserApplication.class);
    }

}