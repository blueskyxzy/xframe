# 官方文档地址：https://sentinelguard.io/zh-cn/

## 简介
    
    Sentinel 的使用可以分为两个部分:  
    
    核心库（Java 客户端）：不依赖任何框架/库，能够运行于 Java 7 及以上的版本的运行时环境，同时对 Dubbo / Spring Cloud 等框架也有较好的支持（见 主流框架适配）。
    控制台（Dashboard）：Dashboard 主要负责管理推送规则、监控、管理机器信息等。
    
    1.资源 是 Sentinel 中的核心概念之一
    例如，把需要控制流量的代码用 Sentinel API SphU.entry("HelloWorld") 和 entry.exit() 包围起来即可
    您也可以通过我们提供的 注解支持模块
    @SentinelResource("HelloWorld")
    
    2.通过流控规则来指定允许该资源通过的请求次数
    
    
    
## Sentinel 工作主流程

    所有的资源都对应一个资源名称以及一个 Entry。
    Entry 可以通过对主流框架的适配自动创建，也可以通过注解的方式或调用 API 显式创建；每一个 Entry 创建的时候，同时也会创建一系列功能插槽（slot chain）。
    这些插槽有不同的职责，
    
    NodeSelectorSlot 负责收集资源的路径，并将这些资源的调用路径，以树状结构存储起来，用于根据调用路径来限流降级；
    ClusterBuilderSlot 则用于存储资源的统计信息以及调用者信息，例如该资源的 RT, QPS, thread count 等等，这些信息将用作为多维度限流，降级的依据；
    StatisticSlot 则用于记录、统计不同纬度的 runtime 指标监控信息；
    FlowSlot 则用于根据预设的限流规则以及前面 slot 统计的状态，来进行流量控制；
    AuthoritySlot 则根据配置的黑白名单和调用来源信息，来做黑白名单控制；
    DegradeSlot 则通过统计信息以及预设的规则，来做熔断降级；
    SystemSlot 则通过系统的状态，例如 load1 等，来控制总的入口流量；