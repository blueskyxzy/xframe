package com.frame.base.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc HttpServletRequestUtils
 */
public class HttpServletRequestUtils {

    private HttpServletRequestUtils() {
    }

    /**
     * 请求参数转map
     */
    public static Map<String, String> convertRequestParameterMap2Map(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(8);
        Map<String, String[]> requestParams = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            /**
             * // 乱码解决，这段代码在出现乱码时使用。如果 mysign 和 sign 不相等也可以使用这段代码转化
             * // valueStr = new String (valueStr. getBytes ("ISO-8859-1"), "gbk");
             */
            map.put(name, valueStr);
        }

        return map;
    }

}
