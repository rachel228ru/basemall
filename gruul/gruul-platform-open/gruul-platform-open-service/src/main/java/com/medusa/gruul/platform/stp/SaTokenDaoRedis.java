package com.medusa.gruul.platform.stp;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import com.medusa.gruul.platform.conf.MeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Huihuang Wu
 * @description
 * @data: 2020/10/30
 */
@Component
public class SaTokenDaoRedis implements SaTokenDao {
    /**
     * string专用
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * SaSession专用
     */
    RedisTemplate<String, SaSession> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据key获取value ，如果没有，则返回空
     *
     * @param key
     * @return
     */

    @Override
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 写入指定key-value键值对，并设定过期时间(单位：秒)
     *
     * @param key
     * @param value
     * @param timeout
     */
    @Override
    public void setValue(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 删除一个指定的key
     *
     * @param key
     */
    @Override
    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }


    /**
     * 根据指定key的session，如果没有，则返回空
     *
     * @param sessionId
     * @return
     */
    @Override
    public SaSession getSaSession(String sessionId) {
        return redisTemplate.opsForValue().get(sessionId);
    }

    /**
     * 将指定session持久化
     *
     * @param session
     * @param timeout
     */
    @Override
    public void saveSaSession(SaSession session, long timeout) {
        redisTemplate.opsForValue().set(session.getId(), session, timeout, TimeUnit.SECONDS);
    }

    /**
     * 更新指定session
     *
     * @param session
     */
    @Override
    public void updateSaSession(SaSession session) {
        long expire = redisTemplate.getExpire(session.getId());
        // -2 = 无此键
        if (expire == MeConstant.FUER) {
            return;
        }
        redisTemplate.opsForValue().set(session.getId(), session, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除一个指定的session
     *
     * @param sessionId
     */
    @Override
    public void delSaSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }

}
