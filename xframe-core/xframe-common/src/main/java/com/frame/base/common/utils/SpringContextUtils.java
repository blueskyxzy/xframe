package com.frame.base.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @desc SpringContextUtils
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.init(applicationContext);
    }

    /**
     * 由于 SonarLint: Make the enclosing method "static" or remove this set. 代码验证建议
     * 单独封装了个 init 方法
     */
    public static void init(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获取 applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过 name 获取 Bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 通过 class 获取 Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过 name 和 Clazz 返回指定的 Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 判断是否存在 Bean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断是否单例
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取 class
     */
    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

}
