package com.medusa.gruul.common.redis.interfaces;

/**
 * redis 访问器
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 18/6/29 上午11:45
 */
public interface IRedisCommand extends IRedisBaseCommand, IRedisListCommand, IRedisSortedListCommand,
        IRedisCollectionCommand, IRedisHashCommand,IRedisSerializeCommand {

    // -------基本处理-------

    // -------列表处理-------

    // -------集合处理-------

    // -------有序集合处理-------

    // -------哈希处理-------

}
