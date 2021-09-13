package com.medusa.gruul.common.redis.interfaces;


/**
 *
 * @author wangpeng
 * @data 2019-10-25下午1:44:38
 * @description redis 存储对象
 * @version V1.0
 */
public interface IRedisSerializeCommand {

    /**
     * 存储对象
     * */
    public  void setObject(String key,Object object);


    /**
     * 获取对象
     * */
    public <T> T  getObject(String key,T t);
}
