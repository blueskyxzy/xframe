package com.frame.base.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.frame.base.common.exception.BaseException;
import com.frame.base.common.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @desc JwtUtils
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "frame.jwt")
public class JwtUtils {

    /**
     * 单位秒
     */
    private int expire;
    /**
     * 加密秘钥
     */
    private String secret;

    public String generateToken(String info) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = DateUtil.offset(nowDate, DateField.MILLISECOND, expire * 1000);
        return Jwts
                .builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(info)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    /**
     * 刷新 token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Assert.hasText(token, "token 不能为空");

        Claims claims = getClaimByToken(token);
        if (claims == null || isTokenExpired(claims.getExpiration())) {
            log.error("token 无效，无法刷新 : " + token);
            throw new BaseException("token 无效，无法刷新");
        }

        return generateToken(claims.getSubject());
    }

    /**
     * 检查 token 是否正确
     */
    public TokenInfo checkToken(HttpServletRequest request) {
        String token = TokenUtils.checkToken(request);
        Claims claims = this.getClaimByToken(token);
        if (claims == null || this.isTokenExpired(claims.getExpiration())) {
            throw new BizException("token 失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }
        return new TokenInfo(token, claims.getSubject());
    }

    @Data
    @AllArgsConstructor
    public class TokenInfo {

        /**
         * token
         */
        String token;

        /**
         * token 解析出来的信息
         */
        String info;

    }

}
