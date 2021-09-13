package com.medusa.gruul.common.redis.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * redis配置枚举
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 2019/10/27 下午11:28
 */
@Data
@RefreshScope
@Component
public class RedisConfig {


    /**
     * redis 默认是否内网
     */
    @Value("${redis.network.isintra}")
    private    boolean  isIntra = false;
    /**
     * redis 默认内网地址
     */
    @Value("${redis.intranet.host}")
    private   String  intranet ;
    /**
     * redis 默认外网地址
     */
    @Value("${redis.outernet.host}")
    private   String outernet ;
    /**
     * redis 默认端口
     */
    @Value("${redis.port}")
    private   Integer  port ;
    /**
     * redis 默认连接超时时间
     */
    @Value("${redis.timeout}")
    private   Integer  timeOut ;
    /**
     * redis 默认连接密码
     */
    @Value("${redis.password}")
    private   String   password ;
    /**
     * redis 默认数据库
     */
    @Value("${redis.database}")
    private   Integer   database ;
}
