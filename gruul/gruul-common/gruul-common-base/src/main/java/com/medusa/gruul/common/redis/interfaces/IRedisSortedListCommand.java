package com.medusa.gruul.common.redis.interfaces;

import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * 
 * @author wangpeng
 * @data 2018-07-08下午1:47:02
 * @description 有序列表
 * @version V1.0
 */
public interface IRedisSortedListCommand extends IRedisBaseCommand {

    public Long zadd(String key, double score, String member);

    public Double zincrby(String key, double score, String member);

    // ZRANGEBYSCORE
    public Set<String> zrangeByScore(String key, String min, String max);

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

}
