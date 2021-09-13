package com.medusa.gruul.common.core.sms;

/**
 * Copyright(C) 2019-12-30 22:18 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-30 22:18
 **/
public class SmsSendConfig {

    /***
     * 短信参数分隔符
     */
    public  static String  SMS_PARAM_SEPARATOR="|";
    public  static String  SMS_PARAM_SPLIT_SEPARATOR="\\|";
    /***
     * 短信参数分隔符
     */
    public  static String  SMS_PHONE_SEPARATOR=",";
   //====================阿里云短信======================
    /***
     * 阿里短信配置
     */
    public  static String  ALI_YUN_REGION_ID = "cn-hangzhou";

    /***
     * 阿里短信配置
     */
    public  static String DOMAIN="dysmsapi.aliyuncs.com";

    /***
     * 阿里短信配置
     */
    public   static String VERSION="2017-05-25";


    public   static int SMS_TYPE_ALI =1;

    /***
     * 阿里发送短信
     */
    public   static String METHOD_SEND_SMS="sendSms";

    /***
     * 阿里推送模版
     */
    public   static String METHOD_ADD_SMS_TEMPLATE="AddSmsTemplate";


    //====================腾讯云短信======================

    /***
     * 腾讯短信
     */
    public static String ZONE = "86";

    public  static int SMS_TYPE_TX = 2;


}
