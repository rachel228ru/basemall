package com.medusa.gruul.common.redis;


import java.util.Date;

public class RedisTest extends RedisVisitorBaseFacade{


    public static final String KEY_BASE = "video";

    public RedisTest() {
        this(KEY_BASE);
    }


    public RedisTest(String baseKey) {
        super(baseKey);
    }

    public static void main(String[] args) {
        RedisTest redisTest = new RedisTest();
        redisTest.set("2","test2");
        String res = redisTest.get("2");
        System.out.println("=========get=set================start");
        System.out.println(res);
        System.out.println("=========get=set================end");
        RedisTestVo v = new RedisTestVo();
        v.setName("testobj");
        v.setId(2L);
        v.setPs(1.0);
        v.setTime(new Date());
        System.out.println("=========get=set==Object==============start");
        System.out.println(v);
        redisTest.setObject("kesto",v);
        v.clear();
        System.out.println(v);
        v= redisTest.getObject("kesto", v);
        System.out.println(v);
        System.out.println("=========get=set==Object==============end");


    }



}
