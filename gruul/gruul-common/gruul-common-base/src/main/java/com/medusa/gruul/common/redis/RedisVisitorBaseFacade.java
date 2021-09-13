package com.medusa.gruul.common.redis;

import com.medusa.gruul.common.redis.interfaces.IRedisCommand;
import com.medusa.gruul.common.redis.model.KeyMapPair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * redis访问器
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 16/6/29 上午11:49
 */
public abstract class RedisVisitorBaseFacade implements IRedisCommand {
    private final static Logger logger = LoggerFactory.getLogger(RedisVisitorBaseFacade.class);
    // redis 管理器
    private RedisManager redisManager = RedisManager.getInstance();

    /**
     * 关键字基础数据
     */
    private String baseKey;

    public RedisVisitorBaseFacade(String baseKey) {
        this.baseKey = baseKey;
    }

    public String genKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            return getBaseKey() + ":" + key;
        }

        return getBaseKey();
    }

    public String getBaseKey() {
        return baseKey;
    }

    @Override
    public String type(String key) {
        try {
            return redisManager.get(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        try {
            return redisManager.get(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 设置key值
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        try {
            redisManager.set(genKey(key), value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 时间单位为毫秒
     */
    @Override
    public void setNxPx(String key, String value, long time) {
        try {
            redisManager.setNxPx(genKey(key), value, time);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Long expire(String key, Integer seconds) {
        try {
            return redisManager.expire(genKey(key), seconds);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long incr(String key) {
        try {
            return redisManager.incr(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long decr(String key) {
        try {
            return redisManager.decr(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 删除keys的对象
     *
     * @param keys
     * @return
     */
    @Override
    public Long del(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return 0L;
            }

            Set<String> keySet = new HashSet<>();
            for (String key : keys) {
                keySet.add(genKey(key));
            }
            return redisManager.del(keySet.toArray(new String[0]));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long delByKeyPattern(String keyPattern) {
        try {
            if (StringUtils.isBlank(keyPattern)) {
                return 0L;
            }
            return redisManager.delByKeyPattern(keyPattern);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String flushdb() {
        try {
            return redisManager.flushdb();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Set<String> keys(String keyPattern) {
        try {
            Set<String> result = new HashSet<>();

            if (StringUtils.isBlank(keyPattern)) {
                return result;
            }

            return redisManager.keys(keyPattern);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new HashSet<>();
    }

    /**
     * 列表-在key列表左侧插入元素values
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long lpush(String key, String... values) {
        try {
            return redisManager.lpush(genKey(key), values);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key列表从左侧取出一个值
     *
     * @param key
     * @return
     */
    @Override
    public String lpop(String key) {
        try {
            return redisManager.lpop(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-在key列表右侧插入元素values
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long rpush(String key, String... values) {
        try {
            return redisManager.rpush(genKey(key), values);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-key列表重最右边取出一个数
     *
     * @param key
     * @return
     */
    @Override
    public String rpop(String key) {
        try {
            return redisManager.rpop(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-获取列表key区间start-end的值 start从0开始 end=-1,表示取出所有值,end值大于列表值不会出现越界错误,获取所有值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<String> lrange(String key, long start, long end) {
        try {
            return redisManager.lrange(genKey(key), start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-获取列表key的长度
     *
     * @param key
     * @return
     */
    @Override
    public Long llen(String key) {
        try {
            return redisManager.llen(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-从列表key中获取index的值
     *
     * @param key
     * @param index
     * @return
     */
    @Override
    public String lindex(String key, long index) {
        try {
            return redisManager.lindex(genKey(key), index);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表-删除列表key区间value的值 count>0时:从表头开始向表尾搜索,移除与value值相等的元素,数量为value
     * count<0时:从表尾开始向表头搜索,移除与value值相等的元素,数量为value count=0时:移除表中所有与value相等的值
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    @Override
    public Long lrem(String key, long count, String value) {
        try {
            return redisManager.lrem(genKey(key), count, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-加入数据至集合
     *
     * @param key
     * @param members
     * @return
     */
    @Override
    public Long sadd(String key, String... members) {
        try {
            return redisManager.sadd(genKey(key), members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-获取集合的成员数
     *
     * @param key
     * @return
     */
    @Override
    public Long scard(String key) {
        try {
            return redisManager.scard(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-列出集合的元素
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String key) {
        try {
            return redisManager.smembers(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-判断member是否是集合key的成员
     *
     * @param key
     * @param member
     * @return
     */
    @Override
    public boolean sismember(String key, String member) {
        try {
            return redisManager.sismember(genKey(key), member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 集合-返回给定所有集合的差集 以第一个集合为参照物
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sdiff(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<>();
            for (String key : keys) {
                keySet.add(genKey(key));
            }

            return redisManager.sdiff(keySet.toArray(new String[0]));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回给定所有集合的交集
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sinter(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<>();
            for (String key : keys) {
                keySet.add(genKey(key));
            }
            return redisManager.sinter(keySet.toArray(new String[0]));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回给定所有集合的并集
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sunion(String... keys) {
        try {
            if (ArrayUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }

            Set<String> keySet = new HashSet<>();
            for (String key : keys) {
                keySet.add(genKey(key));
            }
            return redisManager.sunion(keySet.toArray(new String[0]));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除并返回key集合的的一个随机元素
     *
     * @param key
     * @return
     */
    @Override
    public String spop(String key) {
        try {
            return redisManager.spop(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除并返回key集合的的count个随机元素
     *
     * @param key
     * @param count
     * @return
     */
    @Override
    public Set<String> spop(String key, long count) {
        try {
            return redisManager.spop(genKey(key), count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回集合中一个随机元素
     *
     * @param key
     * @return
     */
    @Override
    public String srandmember(String key) {
        try {
            return redisManager.srandmember(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-返回集合中count个随机元素
     *
     * @param key
     * @param count
     * @return
     */
    @Override
    public List<String> srandmember(String key, int count) {
        try {
            return redisManager.srandmember(genKey(key), count);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 集合-移除key集合指定元素
     *
     * @param key
     * @param members
     * @return
     */
    @Override
    public Long srem(String key, String... members) {
        try {
            return redisManager.srem(genKey(key), members);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        try {
            return redisManager.zadd(genKey(key), score, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        try {
            return redisManager.zincrby(genKey(key), score, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        try {
            return redisManager.zrangeByScore(genKey(key), min, max);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        try {
            return redisManager.zrangeByScoreWithScores(genKey(key), min, max);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-单属性设置哈希key的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hset(String key, String field, String value) {
        try {
            return redisManager.hset(genKey(key), field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希key的field的值
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(String key, String field) {
        try {
            return redisManager.hget(genKey(key), field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取key的所有哈希值
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        try {
            return redisManager.hgetAll(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据key和对象初始值,获取对象转换
     *
     * @param t
     * @param key
     * @return
     */
    @Override
    public <V> V hgetAll(V t, String key) {
        try {
            return redisManager.hgetAll(t, genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key的所有字段
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> hkeys(String key) {
        try {
            return redisManager.hkeys(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key的所有值
     *
     * @param key
     * @return
     */
    @Override
    public List<String> hvals(String key) {
        try {
            return redisManager.hvals(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表中字段的数量
     *
     * @param key
     * @return
     */
    @Override
    public Long hlen(String key) {
        try {
            return redisManager.hlen(genKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取哈希表key给定字段的值
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<String> hmget(String key, String... fields) {
        try {
            return redisManager.hmget(genKey(key), fields);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-获取key,指定属性并返回对象
     *
     * @param t
     * @param key
     * @param fields
     * @return
     */
    @Override
    public <V> V hmget(V t, String key, String... fields) {
        try {
            return redisManager.hmget(t, genKey(key), fields);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-多属性设置哈希的值 key为KeyMapPair-key hash为KeyMapPair-map
     *
     * @param keyMapPair
     * @return
     */
    @Override
    public String hmset(KeyMapPair keyMapPair) {
        try {
            return redisManager.hmset(keyMapPair);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-设置hash对象值hash表中
     *
     * @param t
     * @return @throws
     */
    @Override
    public <V> String hmset(V t) {
        try {
            return redisManager.hmset(t);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-多属性设置哈希key的值
     *
     * @param key
     * @param hash
     * @return
     */
    @Override
    public String hmset(String key, Map<String, String> hash) {
        try {
            return redisManager.hmset(genKey(key), hash);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-只有在字段field不存在时,设置哈希表字段的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hsetnx(String key, String field, String value) {
        try {
            return redisManager.hsetnx(genKey(key), field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-为哈希表key指定的field字段加上增加值value
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hincrBy(String key, String field, long value) {
        try {
            return redisManager.hincrBy(genKey(key), field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希-查询哈希表key中,指定的field是否存在
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Boolean hexists(String key, String field) {
        try {
            return redisManager.hexists(genKey(key), field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 哈希表-删除一个或多个哈希表key的字段
     *
     * @param key
     * @param fields
     */
    @Override
    public Long hdel(String key, String... fields) {
        try {
            return redisManager.hdel(genKey(key), fields);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        try {
            redisManager.subscribe(jedisPubSub, channels);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void setObject(String key, Object object) {
        try {
            redisManager.setObject(genKey(key), object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    @Override
    public <T> T  getObject(String key,T t)  {
        try {
            return redisManager.getObject(genKey(key),t);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
