package com.medusa.gruul.platform.conf;

import com.medusa.gruul.platform.handler.LogHandler;
import com.medusa.gruul.platform.handler.MiniAuditHandler;
import com.medusa.gruul.platform.handler.MpMsgHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.common.redis.WxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binary Wang
 */
@Configuration
@EnableConfigurationProperties({WechatOpenProperties.class, RedisProperties.class, WxMpProperties.class})
public class WxConfiguration {


    @Autowired
    private WechatOpenProperties wechatOpenProperties;
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private MiniAuditHandler miniAuditHandler;
    @Autowired
    private MpMsgHandler mpMsgHandler;
    @Autowired
    private LogHandler logHandler;

    private static JedisPool pool;

    /**
     * 平台配置
     *
     * @return WxOpenService
     */
    @Bean
    public WxOpenService wxOpenService() {
        WxOpenInRedisConfigStorage open = new WxOpenInRedisConfigStorage(getJedisPool());
        open.setComponentAppId(this.wechatOpenProperties.getComponentAppId());
        open.setComponentAppSecret(this.wechatOpenProperties.getComponentSecret());
        open.setComponentToken(this.wechatOpenProperties.getComponentToken());
        open.setComponentAesKey(this.wechatOpenProperties.getComponentAesKey());
        WxOpenService wxOpenService = new WxOpenServiceImpl();
        wxOpenService.setWxOpenConfigStorage(open);
        return wxOpenService;
    }

    private synchronized JedisPool getJedisPool() {
        if (pool == null) {
            pool = new JedisPool(redisProperties, redisProperties.getHost(), Integer.parseInt(redisProperties.getPort()),
                    redisProperties.getConnectionTimeout(), redisProperties.getPassword(),
                    redisProperties.getDatabase(), redisProperties.getClientName());
        }
        return pool;
    }

    /**
     * 开发平台相关
     *
     * @param wxOpenService me.chanjar.weixin.open.api.WxOpenService
     * @return me.chanjar.weixin.open.api.impl.WxOpenMessageRouter
     */
    @Bean
    public WxOpenMessageRouter wxOpenMessageRouter(WxOpenService wxOpenService) {
        final WxOpenMessageRouter wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);
        // 记录所有事件的日志 （异步执行）
        wxOpenMessageRouter.rule().handler(this.logHandler).next();
        //小程序审核路由
        wxOpenMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).handler(this.miniAuditHandler).end();
        return wxOpenMessageRouter;
    }

    /**
     * 公众号相关消息路由
     *
     * @param wxMpService me.chanjar.weixin.mp.api.WxMpService
     * @return me.chanjar.weixin.mp.api.WxMpMessageRouter
     */
    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 自定义消息回复
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(this.mpMsgHandler).end();
        return newRouter;
    }

    @Bean
    public WxMpService wxMpService() {
        final List<WxMpProperties.MpConfig> configs = this.wxMpProperties.getConfigs();
        WxMpService service = new WxMpServiceImpl();
        WxRedisOps wxRedisOps = new JedisWxRedisOps(getJedisPool());
        service.setMultiConfigStorages(configs
                .stream().map(a -> {
                    WxMpDefaultConfigImpl configStorage = new WxMpRedisConfigImpl(wxRedisOps, "platfrom");
                    configStorage.setAppId(a.getAppId());
                    configStorage.setSecret(a.getSecret());
                    configStorage.setToken(a.getToken());
                    configStorage.setAesKey(a.getAesKey());
                    return configStorage;
                }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

}
