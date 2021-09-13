package com.medusa.gruul.common.redis.interfaces;

import redis.clients.jedis.JedisPubSub;

import java.util.Set;

/**
 * 
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public interface IRedisBaseCommand {

    /**
     * 获取类型
     * 
     * @param key
     * @return
     */
    String type(String key);

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置key值
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * set nx px key
     *
     * @param key
     * @param value
     * @param time
     */
    void setNxPx(String key, String value, long time);

    /**
     * 删除keys的对象
     *
     * @param keys
     * @return
     */
    Long del(String... keys);

    Long delByKeyPattern(String keyPattern);

    String flushdb();

    Set<String> keys(String keyPattern);

    Long expire(String key, Integer seconds);

    /**
     * 递增
     *
     * @param key
     * @return
     */
    Long incr(String key);
    /**
     * 递减
     *
     * @param key
     * @return
     */
    Long decr(String key);

    void subscribe(JedisPubSub jedisPubSub, String... channels);

}
