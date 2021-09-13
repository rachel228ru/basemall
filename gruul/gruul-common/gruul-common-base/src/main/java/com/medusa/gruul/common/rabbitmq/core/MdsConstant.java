package com.medusa.gruul.common.rabbitmq.core;


/**
 *
 * 公共配置类
 *
 * @author admin
 * @date 2016/12/22
 */
public class MdsConstant {


    //MQ选择器
    public static final String MQ_FL_EXCHANGE = "FL_EXC";     //分流选择器
    public static final String MQ_DL_EXCHANGE = "DL_EXC";     //死信选择器

    //选择键
    public static final String MQ_FL_SLOW_SEVERITIE = "FL_SLOW_SEV";     //分流慢通道选择键
    public static final String MQ_DL_SLOW_SEVERITIE = "FL_SLOW_SEV";
    public static final String RABBITMQGYS = "RABBITMQGYS";
    public static final String RABBITMQNOTIFY = "RABBITMQNOTIFY";
    public static final String RABBITMQGYSHA = "RABBITMQGYSHA";
    public static final String RABBITMQCALLBACKHA = "RABBITMQCALLBACKHA";
    public static final String RABBITMQLOG = "RABBITMQLOG";
    public static final String RABBITMQFL = "RABBITMQFL";
    public static boolean DB_ASYNC = false;  //数据库操作是否异步 true 异步操作 false 同步操作







}
