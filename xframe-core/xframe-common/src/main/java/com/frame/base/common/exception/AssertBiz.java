package com.frame.base.common.exception;

import org.springframework.lang.Nullable;

/**
 * @desc AssertBiz
 */
public class AssertBiz {

    private AssertBiz() {

    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new BizException(message, false);
        }
    }

    public static void hasText(@Nullable String text, String message) {
        if (!org.springframework.util.StringUtils.hasText(text)) {
            throw new BizException(message, false);
        }
    }

    public static void isNotBlank(@Nullable String text, String message) {
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            throw new BizException(message, false);
        }
    }

    public static void isNotBlank(@Nullable String text, String message, boolean needLog) {
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            throw new BizException(message, needLog);
        }
    }

}
