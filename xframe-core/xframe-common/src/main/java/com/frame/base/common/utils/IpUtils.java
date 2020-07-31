package com.frame.base.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc IpUtils
 */
@Slf4j
public class IpUtils {

    private IpUtils() {
        
    }

    private static final String UNKNOWN = "unknown";

    /**
     * 获取IP地址
     * 使用 nginx 等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

        return ip;
    }

    /**
     * 获取域名
     */
    public static String getServerName(HttpServletRequest request) {
        return request.getServerName();
    }

    /**
     * 根据IP地址获取地区
     */
    public static String getAddressNameByIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            log.error("ip不能为空");
            return null;
        }

        String result = HttpUtil.get("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
        if (StringUtils.isBlank(result)) {
            log.error("地址获取失败");
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(result);
        int code = jsonObject.getIntValue("code");
        if (0 != code) {
            log.error("地址获取失败 : " + code);
            return null;
        }

        JSONObject object = jsonObject.getJSONObject("data");
        String region = object.getString("region");
        String city = object.getString("city");
        String isp = object.getString("isp");
        if (StringUtils.isBlank(region) && StringUtils.isBlank(city) && StringUtils.isBlank(isp)) {
            log.error("地址获取为空");
            return null;
        }
        return region + city + " " + isp;
    }

}
