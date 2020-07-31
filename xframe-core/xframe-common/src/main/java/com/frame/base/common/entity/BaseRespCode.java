package com.frame.base.common.entity;

/**
 * @desc 错误码
 */
public final class BaseRespCode {

    private BaseRespCode() {

    }

    /**
     * 给前端展示错误用
     */
    public static final int BIZ = 88;

    /**
     * 被黑名单拦截
     */
    public static final int BIZ_INTERCEPT_BLACK = 1_000;
    /**
     * 不在白名单里
     */
    public static final int BIZ_INTERCEPT_WHITE = 1_001;

}
