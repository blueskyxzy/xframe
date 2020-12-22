package com.xzy.xframe.demo.order;

import com.daoism.base.datasource.config.DynamicDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xzy on 2020/3/31  .
 */

@EnableElasticsearchRepositories(basePackages = { "com.xzy.xframe.demo.order.dao" })
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
@EnableFeignClients
//@ComponentScan(basePackages={"com.xzy.xframe.api"})
//@ComponentScan(basePackages = {"com.xzy.xframe.gateway.common.filter"})
public class OrderApplication {

    public static void main(String[] args){
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
