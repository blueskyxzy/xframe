package com.frame.base.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.frame.base.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.*;

/**
 * @desc SignUtils
 */
@Slf4j
public class SignUtils {

    public static final String TYPE_MD5 = "md5";
    public static final String TYPE_SHA = "sha";

    /**
     * 签名算法 最后拼接 &key=xxx
     * 类似微信 https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=4_3
     */
    public static final int ARITHMETIC_0 = 0;
    /**
     * 签名算法 最后拼接 key
     * 类似银联
     */
    public static final int ARITHMETIC_1 = 1;

    /**
     * 通过公钥验证签名
     */
    public static boolean verifySign(SortedMap<String, Object> paramMap, String key, String type) {
        String sign = (String) paramMap.get("sign");
        if (StringUtils.isBlank(sign)) {
            log.error("sign 不能为空");
            return false;
        }
        if (StringUtils.isBlank(key)) {
            log.error("key不能为空");
            return false;
        }

        // 添加参数 key
        paramMap.put("key", key);
        // 把参数重新签名进行比对
        String signNew = createSign(paramMap, key, type);
        return Objects.equals(sign, signNew);
    }

    /**
     * 生成签名 Map 算法 0
     */
    public static String createSign(Map<String, Object> paramMap, String key, String type) {
        return createSign(new TreeMap<>(paramMap), key, ARITHMETIC_0, type);
    }

    /**
     * 生成签名 Map
     */
    public static String createSign(Map<String, Object> paramMap, String key, int arithmetic, String type) {
        return createSign(new TreeMap<>(paramMap), key, arithmetic, type);
    }

    /**
     * 生成签名 算法 0
     */
    public static String createSign(SortedMap<String, Object> paramMap, String key, String type) {
        return createSign(paramMap, key, ARITHMETIC_0, type);
    }

    /**
     * 生成签名 SortedMap
     */
    public static String createSign(SortedMap<String, Object> paramMap, String key, int arithmetic, String type) {
        if (StringUtils.isBlank(key)) {
            log.error("key不能为空");
            return null;
        }

        // 组装参数,忽略参数名或参数值为空的参数
        String paramStr;
        if (arithmetic == ARITHMETIC_0) {
            paramStr = buildParamBy0(paramMap, key);
        } else if (arithmetic == ARITHMETIC_1) {
            paramStr = buildParamBy1(paramMap, key);
        } else {
            log.error("签名算法不支持" + type);
            return null;
        }

        switch (type) {
            case TYPE_MD5:
                return createSignByMd5(paramStr);
            case TYPE_SHA:
                return createSignBySha(paramStr);
            default:
                log.error("签名类型不支持" + type);
                return null;
        }
    }

    /**
     * 通过算法 0 组装参数
     */
    private static String buildParamBy0(SortedMap<String, Object> paramMap, String key) {
        // 要先去掉参数里的 sign
        paramMap.remove("sign");
        // 添加参数 key
        paramMap.put("key", key);
        // 组装参数,忽略参数名或参数值为空的参数
        return ParamUtils.buildParamNotEncode(paramMap);
    }

    /**
     * 通过算法 1 组装参数
     */
    private static String buildParamBy1(SortedMap<String, Object> paramMap, String key) {
        // 要先去掉参数里的 sign
        paramMap.remove("sign");
        // 组装参数,忽略参数名或参数值为空的参数
        return ParamUtils.buildParamNotEncode(paramMap) + key;
    }

    /**
     * 通过MD5生成签名
     */
    private static String createSignByMd5(String paramStr) {
        return SecureUtil.md5(paramStr).toUpperCase();
    }

    /**
     * 通过公钥生成签名
     * //private static String createSignBySha(String paramStr, String key) {
     * //    try {
     * //        Mac sha256 = Mac.getInstance("HmacSHA256");
     * //        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
     * //        sha256.init(secretKeySpec);
     * //        byte[] bytes = sha256.doFinal(paramStr.getBytes(StandardCharsets.UTF_8));
     * //        return HexUtil.encodeHexStr(bytes).toUpperCase();
     * //    } catch (Exception e) {
     * //        log.error("createSign error : " + e);
     * //        return null;
     * //    }
     * //}
     */
    private static String createSignBySha(String paramStr) {
        try {
            return DigestUtils.sha256Hex(paramStr.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        } catch (Exception e) {
            throw new BaseException("签名过程中出现错误");
        }
    }

    /**
     * 自动生成密钥对[私钥,公钥]
     */
    public static String[] generateKeyPairOfRsa() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        String privateKey = Base64.encode(pair.getPrivate().getEncoded());
        String publicKey = Base64.encode(pair.getPublic().getEncoded());
        return new String[]{privateKey, publicKey};
    }

    public static void main(String[] args) {
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALLGFMNt6iKlugFLKJQrebHOs9sVYoPnO/ZcVMY+3BFzgz3UyS2ktH1tTf3sPa38s3F0aYiIJrckyRTV56MbFew+6vrvwI7hxyNP6oL79E94C9DZzbFRxd8BhmV5bJ+CQBpR/JAGdbQpc36MrrjYqAMOhCSC1mYOJYke3KgEWapvAgMBAAECgYA4nx4ya5ytjpnkEP7wNvjbRSGX8ST7rABE3ieHhp9C87gMnFl/mFwGIdmC5BEBCzl2ZK2MLbs9ZGw/qiQsd8awPKZtFbLeai/9f6AAnWUZXKhIEom2c3hmUfJPW8xiEznA8ISdH9qcO6kh4e2X4kz5/a5SZZauErX3WjLVnGnomQJBAPdlkhKeOTH8DNG8rx/ZJPlhLw1Q0oXRACbgFbBXd37zcjG6zneLdB2TK8Mtfj8jK39Xafbu9ajgGQFWX1YDgu0CQQC4/ZmNvvb95Pd982olqi9FTywkc2Le3Xof6tsTPstx3lvnTsWigN/U87qoEEvUmLtbhPvp23ri5YjHmM+KlqtLAkBMP+LomBmavUuaLSRwlXWe4wQkf3+TPNpmIPSuWBb9ar0MdL4D/Fg6cUywqEnb3dQfOGrxb68JcKBkWCGkGgr1AkBNEKj1mgZ/QvGvsMYZpspGj3MORFt+CLolrctSOyi56S5UMEa5KgMVTewGs7NDf9UdjUr844hQGdtfe1OKorX9AkAuircDPBwp0Il2ZEK6rS/ZQLRyKTmFptRwvF652CYOTGK6zkcpg552HrLdJIgXEVsgMNTxwKaqgvSjM0qslyBB";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyxhTDbeoipboBSyiUK3mxzrPbFWKD5zv2XFTGPtwRc4M91MktpLR9bU397D2t/LNxdGmIiCa3JMkU1eejGxXsPur678CO4ccjT+qC+/RPeAvQ2c2xUcXfAYZleWyfgkAaUfyQBnW0KXN+jK642KgDDoQkgtZmDiWJHtyoBFmqbwIDAQAB";

        Map<String, Object> map = new HashMap<>(8);
        map.put("3", "33");
        map.put("a", "aa");
        map.put("1", "11");
        map.put("2", "22");
        map.put("b", "bb");

        // MD5 签名验证
        SortedMap<String, Object> sortMd5 = new TreeMap<>(map);
        String signMd5 = createSign(sortMd5, publicKey, TYPE_MD5);
        log.debug("makeSignMD5 = " + signMd5);
        sortMd5.put("sign", signMd5);
        log.debug(verifySign(sortMd5, publicKey, TYPE_MD5) + "");

        // SHA 签名验证
        SortedMap<String, Object> sortSha = new TreeMap<>(map);
        String signSha = createSign(sortSha, publicKey, TYPE_SHA);
        log.debug("makeSignSHA = " + signSha);
        sortSha.put("sign", signSha);
        log.debug(verifySign(sortSha, publicKey, TYPE_SHA) + "");

        // 公钥加密，私钥解密
        RSA rsa = new RSA(privateKey, publicKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes("test", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        log.debug(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
    }

}
