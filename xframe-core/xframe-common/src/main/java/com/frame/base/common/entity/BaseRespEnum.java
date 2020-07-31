package com.frame.base.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc 返回枚举
 */
@Getter
@AllArgsConstructor
public enum BaseRespEnum {

    /**
     * 被黑名单拦截
     */
    INTERCEPT_BLACK(BaseRespCode.BIZ_INTERCEPT_BLACK, "被黑名单拦截，详情咨询管理员"),
    /**
     * 不在白名单里
     */
    INTERCEPT_WHITE(BaseRespCode.BIZ_INTERCEPT_WHITE, "不在白名单里，详情咨询管理员");

    private final int code;
    private final String msg;

}
