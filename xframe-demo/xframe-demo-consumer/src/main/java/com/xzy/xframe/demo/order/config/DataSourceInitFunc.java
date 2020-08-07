package com.xzy.xframe.demo.order.config;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xzy
 * @create: 2020-08-04
 * 1.sentinel的Nacos数据源配置类
 *
 * 2.添加了datasource:
 *         ds:
 *           nacos:
 *             server-addr: localhost:8848
 *             dataId: ${spring.application.name}-sentinel
 *             groupId: DEFAULT_GROUP
 *             ruleType: flow
 *
 * 就不需要这个配置类了
 *
 * 3.硬编码添加流控规则  FlowRuleManager.loadRules(rules);
 * 同一个资源可以同时有多个限流规则。
 *
 *
 * 显然这种nacos动态配置的不需要重新部署，效果更佳
 **/


//@Configuration
public class DataSourceInitFunc {

    Logger logger = LoggerFactory.getLogger(DataSourceInitFunc.class);

    @Autowired
    private SentinelProperties sentinelProperties;

//    @Bean
    public DataSourceInitFunc init() throws Exception {

        logger.info("[NacosSource初始化,从Nacos中获取熔断规则]");


//        sentinelProperties.getDatasource().entrySet().stream().filter(map -> {
//            return map.getValue().getNacos() != null;
//        }).forEach(map -> {
//            NacosDataSourceProperties nacos = map.getValue().getNacos();
//            ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(nacos.getServerAddr(),
//                    nacos.getGroupId(), nacos.getDataId(),
//                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
//                    }));
//            FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
//        });

        return new DataSourceInitFunc();

    }


        /**
        * @Description: 硬编码添加流控规则
        * @Author: xzy
        * @Date: 2020/08/04
        */
    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/seckill");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * @Description: 硬编码添加熔断降级规则
     * @Author: xzy
     * @Date: 2020/08/04
     */
    private void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource("/seckill");
        // set threshold RT, 10 ms
        rule.setCount(10);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }

    /**
     * @Description: 硬编码添加系统保护规则规则
     * @Author: xzy
     * @Date: 2020/08/04
     */
    private void initSystemRule() {
        List<SystemRule> rules = new ArrayList<>();
        SystemRule rule = new SystemRule();
        rule.setHighestSystemLoad(10);
        rules.add(rule);
        SystemRuleManager.loadRules(rules);
    }

}
