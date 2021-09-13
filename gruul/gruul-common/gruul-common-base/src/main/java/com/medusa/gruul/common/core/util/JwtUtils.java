package com.medusa.gruul.common.core.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.medusa.gruul.common.core.constant.TimeConstants;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * @author Huihuang Wu
 * @description jwt工具类
 * @data: 2020/9/19
 */

public class JwtUtils {
    //签名私钥
    private String privateKey;
    //签名有效时间
    private Long ttl;


    /**
     * 固定 ttl 2小时失效
     * privateKey 随机生成24长度字符串
     */
    public JwtUtils() {
        this.privateKey = RandomUtil.randomString(24);
        this.ttl = TimeConstants.ONE_HOUR * 2;
    }

    /**
     * privateKey 随机生成24长度字符串
     *
     * @param ttl 指定时间毫秒
     */
    public JwtUtils(long ttl) {
        this.privateKey = RandomUtil.randomString(24);
        this.ttl = ttl;
    }

    /**
     * 固定 ttl 2小时失效
     *
     * @param privateKey 指定秘钥
     */
    public JwtUtils(String privateKey) {
        this.privateKey = privateKey;
        this.ttl = TimeConstants.ONE_HOUR * 2;
    }

    /**
     * 初始化jwt
     *
     * @param privateKey 私钥
     * @param ttl        有效期(毫秒)
     */
    public JwtUtils(String privateKey, long ttl) {
        this.privateKey = privateKey;
        this.ttl = ttl;
    }

    /**
     * 解析JWT
     *
     * @param jwtStr jwt字符串
     * @return io.jsonwebtoken.Claims
     */
    public Claims parseJwt(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(privateKey.getBytes())
                .parseClaimsJws(jwtStr)
                .getBody();
    }

    /**
     * 生成token
     *
     * @param jti     JWT ID JWT的唯一身份标识
     * @param subject Subject JWT的主题
     * @param map     自定义属性
     * @return jwt str
     */
    public String createJwtToken(String jti, String subject, Map<String, Object> map) {
        if (StrUtil.isEmpty(privateKey) || ttl == null) {
            throw new RuntimeException("jwt未初始化");
        }
        if (StrUtil.isEmpty(jti)) {
            jti = IdUtil.fastSimpleUUID();
        }
        //当前时间+有效时间=过期时间
        //创建JwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(jti)
                .setHeaderParam("type", Header.JWT_TYPE)
                .setSubject(subject)
                .setIssuer("bgniao.com")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, privateKey.getBytes());
        //根据map设置clamis
        if (MapUtil.isNotEmpty(map)) {
            jwtBuilder.addClaims(map);
        }
        //设置失效时间
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + ttl));
        return jwtBuilder.compact();
    }

    /**
     * 生成token
     *
     * @param subject Subject JWT的主题
     * @param map     自定义属性
     * @return jwt str
     */
    public String createJwtToken(String subject, Map<String, Object> map) {
        return createJwtToken(null, subject, map);
    }

    /**
     * 生成token
     *
     * @param subject Subject JWT的主题
     * @return jwt str
     */
    public String createJwtToken(String subject) {
        return createJwtToken(null, subject, null);
    }


    /**
     * 生成token
     *
     * @param map 自定义属性
     * @return jwt str
     */
    public String createJwtToken(Map<String, Object> map) {
        return createJwtToken(null, null, map);
    }


    /**
     * 生成token
     *
     * @return jwt str
     */
    public String createJwtToken() {
        return createJwtToken(null, null, null);
    }
}
