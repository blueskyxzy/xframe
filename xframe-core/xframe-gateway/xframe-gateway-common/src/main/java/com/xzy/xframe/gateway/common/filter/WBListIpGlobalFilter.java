package com.xzy.xframe.gateway.common.filter;

import com.alibaba.fastjson.JSON;
import com.frame.base.common.entity.BaseResp;
import com.frame.base.common.entity.BaseRespEnum;
import com.xzy.xframe.gateway.common.config.MonoUtils;
import com.xzy.xframe.gateway.common.config.WBIpListProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: xzy
 * @Description: 黑白名单IP地址过滤器
 * @Date: Created in 2020/8/1.
 */
@Slf4j
@Component
public class WBListIpGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private WBIpListProperties properties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("WBListIpGlobalFilter");
        String ip = exchange.getRequest().getRemoteAddress().getAddress().toString();
//        String ip = getIpAddress((ServerHttpRequest) exchange.getRequest());
        if (properties == null){
            log.error("WBIpListProperties 读取配置失败");
        } else {
            // 白名单验证
            BaseRespEnum checkByWhite = checkByWhite(properties, ip);
            if (null != checkByWhite) {
                return MonoUtils.returnHttpResponse(exchange, JSON.toJSONString(BaseResp.error(checkByWhite.getCode(), checkByWhite.getMsg())));
            }
            // 黑名单验证
            BaseRespEnum checkByBlack = checkByBlack(properties, ip);
            if (null != checkByBlack) {
                return MonoUtils.returnHttpResponse(exchange, JSON.toJSONString(BaseResp.error(checkByBlack.getCode(), checkByBlack.getMsg())));
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }

    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 如果包含黑名单就不能继续
     */
    private BaseRespEnum checkByBlack(WBIpListProperties properties, String ip) {
        if (!properties.getBlackEnable()) {
            return null;
        }
        if (properties.getBlacklist() == null) {
            return null;
        }
        return properties.getBlacklist().contains(ip) ? BaseRespEnum.INTERCEPT_BLACK : null;
    }

    /**
     * 如果包含白名单就继续
     */
    private BaseRespEnum checkByWhite(WBIpListProperties properties, String ip) {
        if (!properties.getWhiteEnable()) {
            return null;
        }
        if (properties.getWhitelist()== null) {
            return null;
        }
        return properties.getWhitelist().contains(ip) ?  null: BaseRespEnum.INTERCEPT_WHITE;
    }


}