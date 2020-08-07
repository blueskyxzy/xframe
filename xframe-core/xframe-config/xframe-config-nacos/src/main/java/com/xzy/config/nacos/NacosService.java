package com.xzy.config.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author: xzy
 * @create: 2020-08-05
 **/

public class NacosService {

    @Value("${nacos.default.serverAddr}")
    private String serverAddr;

    @Value("${nacos.default.namespace}")
    private String namespace;

    @Value("${nacos.default.group}")
    private String group;

    @Value("${nacos.default.dataId}")
    private String dataId;

    public ConfigService getConfigService() throws NacosException {
        // 初始化配置服务
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("namespace", namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        return configService;
    }

    public String getConfigContent(ConfigService configService) throws NacosException {
        //获取配置
        String content = configService.getConfig(dataId, group, 5000);
        return content;
    }

    public void listener (ConfigService configService) throws NacosException {
        configService.addListener(dataId, group, new Listener() {

            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void receiveConfigInfo(String s) {
                //当配置发生变化时的响应

            }
        });
    }
}
