package com.frame.base.common.utils;

import com.frame.base.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc TokenUtils
 */
public class TokenUtils {

    private TokenUtils() {
    }

    /**
     * 获取请求的token
     */
    public static String getRequestToken(HttpServletRequest request) {
        // 从 header 中获取 token
        String token = request.getHeader("token");
        // 如果 header 中不存在 token，则从参数中获取 token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        } else {
            return token;
        }
        return token;
    }

    /**
     * 检查 token 是否正确
     */
    public static String checkToken(HttpServletRequest request) {
        String token = TokenUtils.getRequestToken(request);
        if (StringUtils.isBlank(token)) {
            throw new BizException("token 不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        return token;
    }

}
