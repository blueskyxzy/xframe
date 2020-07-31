package com.frame.base.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @desc IdUtils
 */
@Slf4j
@Component
public class IdUtils {

    /**
     * 工作机器ID(0~31)
     */
    @Value("${frame.id.workerId: 0}")
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    @Value("${frame.id.dataCenterId: 0}")
    private long dataCenterId;

    public synchronized String nextId() {
        String snowflakeId = IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
        return DateUtil.format(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS") + snowflakeId.substring(15);
    }

    public synchronized String nextId(String idPrefix) {
        return (null == idPrefix ? "" : idPrefix) + nextId();
    }

}