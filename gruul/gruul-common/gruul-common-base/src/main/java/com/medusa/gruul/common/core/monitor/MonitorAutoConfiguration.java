package com.medusa.gruul.common.core.monitor;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.medusa.gruul.common.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 心跳监听启动
 *
 * @author whh
 */
@Slf4j
@Configuration
@Component
@Profile("!unit-test")
@EnableConfigurationProperties(MonitorServiceConfig.class)
public class MonitorAutoConfiguration implements ApplicationRunner {


    @Value("${spring.application.name}")
    private String name;
    /**
     * 版本
     */
    @Value("${spring.cloud.nacos.discovery.metadata.version}")
    private String version;
    @Autowired
    public MonitorServiceConfig monitorServiceConfig;

    /**
     * 基础库唯一标识
     */
    private String uniqueness;


    private void run(MonitorServiceConfig monitorServiceConfig) {
        monitorServiceConfig.setStartIdentifier(RandomUtil.randomNumbers(8));
        RedisManager redisManager = RedisManager.getInstance();
        String librariesRedisKey = "platform:libraries:".concat(version);
        String baseWarehouse = "platform:base-warehouse";
        CronUtil.schedule("serivceHeartbeat", "*/5 * * * * *", () -> {
            consoleLog(monitorServiceConfig.getUseLog(), "------------------start------------------");
            Boolean useRun = monitorServiceConfig.getUseRun();
            if (useRun == null || !useRun) {
                consoleLog(monitorServiceConfig.getUseLog(), "不启动心跳配置");
                return;
            }
            HttpRequest post = HttpUtil.createPost(redisManager.get(baseWarehouse));
            try {
                monitorServiceConfig.setVersion(version);
                monitorServiceConfig.setUniqueness(redisManager.get(librariesRedisKey));
                consoleLog(monitorServiceConfig.getUseLog(), "最终发送的配置:" + monitorServiceConfig.toString());
                post = HttpUtil.createPost(redisManager.get(baseWarehouse));
                post.contentType("application/json;charset=UTF-8");
                post.body(JSONUtil.toJsonPrettyStr(monitorServiceConfig));
                String body = post.execute().body();
                consoleLog(monitorServiceConfig.getUseLog(), "基础库心跳统一入口url请求返回内容: " + body);
                consoleLog(monitorServiceConfig.getUseLog(), "------------------end------------------");
            }catch (Exception e){
                log.info(post.execute().body());
                e.printStackTrace();
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    private void consoleLog(Boolean useLog, String text) {
        if (useLog == null || !useLog) {
            return;
        }
        log.debug(text);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.run(monitorServiceConfig);
    }
}
