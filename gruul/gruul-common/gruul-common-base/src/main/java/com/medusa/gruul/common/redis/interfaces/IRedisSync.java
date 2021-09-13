package com.medusa.gruul.common.redis.interfaces;


import com.medusa.gruul.common.redis.RedisVisitorBaseFacade;
import com.medusa.gruul.common.redis.model.SyncResult;

/**
 * 数据检测
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 18/6/29 下午2:25
 */
public interface IRedisSync {

    /**
     * 获取redis访问器
     *
     * @return
     */
    public RedisVisitorBaseFacade getRedisVisitor();

    /**
     * 是否存在于redis
     *
     * @param key
     * @return
     */
    public boolean isInRedis(String key);

    /**
     * 同步
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> SyncResult<T> sync(String key);

    /**
     * 检测是否存在于redis 若存在则不做操作 若不存在进行数据同步
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> SyncResult<T> checkAndSync(String key);

    /**
     * 重新同步
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> SyncResult<T> resync(String key);

}
