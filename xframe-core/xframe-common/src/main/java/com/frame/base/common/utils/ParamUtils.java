package com.frame.base.common.utils;

import cn.hutool.core.util.URLUtil;

import java.util.Map;

/**
 * @desc ParamUtils
 */
public class ParamUtils {

    private ParamUtils() {
    }

    /**
     * 组装参数,忽略参数名或参数值为空的参数 编码
     */
    public static String buildParamWithEncode(Map<String, Object> paramMap) {
        StringBuilder paramStr = new StringBuilder();
        paramMap.forEach((k, v) -> {
            if (null != k && null != v) {
                if (paramStr.length() > 0) {
                    paramStr.append("&");
                }
                paramStr.append(k).append("=").append(URLUtil.encode(String.valueOf(v)));
            }
        });
        return paramStr.toString();
    }

    /**
     * 组装参数,忽略参数名或参数值为空的参数 不编码
     */
    public static String buildParamNotEncode(Map<String, Object> paramMap) {
        StringBuilder paramStr = new StringBuilder();
        paramMap.forEach((k, v) -> {
            if (null != k && null != v) {
                if (paramStr.length() > 0) {
                    paramStr.append("&");
                }
                paramStr.append(k).append("=").append(v);
            }
        });
        return paramStr.toString();
    }

}
