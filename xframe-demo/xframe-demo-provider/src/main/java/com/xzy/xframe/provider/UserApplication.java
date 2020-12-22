package com.xzy.xframe.provider;

import com.xzy.demo.config.DynamicDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by xzy on 2020/3/31  .
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
public class UserApplication {

    public static void main(String[] args){
        SpringApplication.run(UserApplication.class);
    }

}