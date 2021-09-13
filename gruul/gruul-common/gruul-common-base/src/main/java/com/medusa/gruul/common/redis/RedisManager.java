package com.medusa.gruul.common.redis;

import com.medusa.gruul.common.core.util.SpringContextHolder;
import com.medusa.gruul.common.redis.constant.RedisConfig;
import com.medusa.gruul.common.redis.exception.MissAnnotationException;
import com.medusa.gruul.common.redis.interfaces.IRedisCommand;
import com.medusa.gruul.common.redis.model.KeyMapPair;
import com.medusa.gruul.common.redis.parse.HashMapParser;
import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis manager
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 16/6/23 下午12:00
 */

public class RedisManager implements IRedisCommand {

    private JedisPool pool;
    private String host;

    private static class RedisManagerHolder {
        private static final RedisManager INSTANCE = new RedisManager();
    }

    public static RedisManager getInstance() {
        return RedisManagerHolder.INSTANCE;
    }

    private RedisManager() {
        RedisConfig bean = SpringContextHolder.getBean(RedisConfig.class);
        // 设置主机
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(40);
        poolConfig.setMaxIdle(40);
        host = bean.isIntra() ? bean.getIntranet() : bean.getOuternet();
        pool = new JedisPool(poolConfig, host, bean.getPort(), bean.getTimeOut(), bean.getPassword(),
                bean.getDatabase());
    }

    @Override
    public void setObject(String key, Object object) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key.getBytes(), HashMapParser.serizlize(object));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public <T> T getObject(String key, T t) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            return HashMapParser.deserialize(bytes, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String type(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String val = jedis.type(key);
            return val;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String val = jedis.get(key);
            return val;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置key值
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            // String ret =
            jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 自增
     */
    @Override
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.incr(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 递减
     */
    @Override
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.decr(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void setNxPx(String key, String value, long time) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value, "NX", "PX", time);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Long expire(String key, Integer seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.expire(key, seconds);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Long del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.del(keys);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Long delByKeyPattern(String keyPattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> keys = jedis.keys(keyPattern);
            Long ret = jedis.del(keys.toArray(new String[0]));
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String flushdb() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.flushDB();
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取keys
     *
     * @param keyPattern
     * @return
     */
    @Override
    public Set<String> keys(String keyPattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> ret = jedis.keys(keyPattern);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // -------列表处理-------

    /**
     * 列表-在key列表左侧插入元素values
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long lpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            Long ret = jedis.lpush(key, values);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 列表-key列表从左侧取出一个值
     *
     * @param key
     * @return
     */
    @Override
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.lpop(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.rpush(key, values);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 列表-key列表重最右边取出一个数
     *
     * @param key
     * @return
     */
    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.rpop(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            List<String> ret = jedis.lrange(key, start, end);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 列表-获取列表key的长度
     *
     * @param key
     * @return
     */
    @Override
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

            Long ret = jedis.llen(key);

            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.lindex(key, index);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.lrem(key, count, value);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // -------集合处理-------

    /**
     * 集合-加入数据至集合
     *
     * @param key
     * @param members
     * @return
     */
    @Override
    public Long sadd(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.sadd(key, members);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 集合-获取集合的成员数
     *
     * @param key
     * @return
     */
    @Override
    public Long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.scard(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 集合-列出集合的元素
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> ret = jedis.smembers(key);

            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            boolean ret = jedis.sismember(key, member);

            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 集合-返回给定所有集合的差集 以第一个集合为参照物
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiff(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 集合-返回给定所有集合的交集
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinter(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 集合-返回给定所有集合的并集
     *
     * @param keys
     * @return
     */
    @Override
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunion(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 集合-移除并返回key集合的的一个随机元素
     *
     * @param key
     * @return
     */
    @Override
    public String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.spop(key);

            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key, count);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 集合-返回集合中一个随机元素
     *
     * @param key
     * @return
     */
    @Override
    public String srandmember(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            List<String> ret = jedis.srandmember(key, count);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.srem(key, members);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // -------有序集合处理-------
    @Override
    public Long zadd(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.zadd(key, score, member);

            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            double ret = jedis.zincrby(key, score, member);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * min和max可以是-inf和+inf
     */
    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> ret = jedis.zrangeByScore(key, min, max);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<Tuple> ret = jedis.zrangeByScoreWithScores(key, min, max);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // -------哈希处理-------

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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.hset(key, field, value);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.hget(key, field);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 哈希-获取key的所有哈希值
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Map<String, String> ret = jedis.hgetAll(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 根据key和对象初始值,获取对象转换
     *
     * @param t
     * @param key
     * @param <T>
     * @return
     */
    @Override
    public <T> T hgetAll(T t, String key) {
        Map<String, String> hash = hgetAll(key);
        T ret = HashMapParser.hash2Obj(t, hash);
        return ret;
    }

    /**
     * 哈希-获取哈希表key的所有字段
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hkeys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 哈希-获取哈希表key的所有值
     *
     * @param key
     * @return
     */
    @Override
    public List<String> hvals(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hvals(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 哈希-获取哈希表中字段的数量
     *
     * @param key
     * @return
     */
    @Override
    public Long hlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.hlen(key);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 哈希-获取key,指定属性并返回对象
     *
     * @param t
     * @param key
     * @param fields
     * @param <T>
     * @return
     */
    @Override
    public <T> T hmget(T t, String key, String... fields) {
        List<String> rets = hmget(key, fields);

        Map<String, String> hash = new HashMap<>();

        int i = 0;
        for (String field : fields) {
            hash.put(field, rets.get(i++));
        }

        T ret = HashMapParser.hash2Obj(t, hash);


        return ret;
    }


    /**
     * 哈希-多属性设置哈希的值 key为KeyMapPair-key hash为KeyMapPair-map
     *
     * @param keyMapPair
     * @return
     */
    @Override
    public String hmset(KeyMapPair keyMapPair) {
        return hmset(keyMapPair.getKey(), keyMapPair.getMap());
    }

    /**
     * 哈希-设置hash对象值hash表中
     *
     * @param t
     * @param <T>
     * @return
     * @throws MissAnnotationException
     */
    @Override
    public <T> String hmset(T t) throws MissAnnotationException {
        KeyMapPair keyMapPair = HashMapParser.obj2Hash(t);
        return hmset(keyMapPair);
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String ret = jedis.hmset(key, hash);
            // LoggerConfigs.REDIS_LOG.info("hmset:{} hash:{} ret:{}", new
            // Object[]{key, hash, ret});
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.hsetnx(key, field, value);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.hincrBy(key, field, value);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
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
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Boolean ret = jedis.hexists(key, field);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 哈希表-删除一个或多个哈希表key的字段
     *
     * @param key
     * @param fields
     */
    @Override
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Long ret = jedis.hdel(key, fields);
            return ret;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channels);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String toString() {
        return "RedisManager{" +
                ", host='" + host + '\'' +
                '}';
    }
}
