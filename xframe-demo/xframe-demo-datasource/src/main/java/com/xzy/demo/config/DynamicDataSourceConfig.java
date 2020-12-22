package com.xzy.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.xzy.demo.DataSourceNames;
import com.xzy.demo.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 配置多数据源
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.first")
    public DataSource firstDataSource() {
        DruidDataSource datasource = DruidDataSourceBuilder.create().build();
        datasource.setConnectionInitSqls(Collections.list(makeUtf8mb4Config()));
        return datasource;
    }

    // 暂时不需要额外的数据源
    /*@Bean
    @ConfigurationProperties("spring.datasource.druid.second")
    public DataSource secondDataSource() {
        DruidDataSource datasource = DruidDataSourceBuilder.create().build();
        datasource.setConnectionInitSqls(Collections.list(makeUtf8mb4Config()));
        return datasource;
    }*/

    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceNames.FIRST, firstDataSource);
        targetDataSources.put(DataSourceNames.SECOND, secondDataSource);
        return new DynamicDataSource(firstDataSource, targetDataSources);
    }

    /**
     * 重点设置,支持 emoji
     */
    private StringTokenizer makeUtf8mb4Config() {
        String connectionInitSqls = "SET NAMES utf8mb4";
        return new StringTokenizer(connectionInitSqls, ";");
    }

}
