package com.medusa.gruul.sms.constant;

/**
 * Copyright(C) 2020-01-01 12:46 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2020-01-01 12:46
 **/
public class SmsSmsStatus {
    /**
     * 待发送
     */
    public static int WAIT_SEND = 0;
    /**
     * 发送中
     */
    public static int SEN_DING = 1;
    /**
     * 提供通道商成功
     */
    public static int SEND_CHANNAL = 2;
    /**
     * 通道返回成功
     */
    public static int SEND_CHANNAL_RES = 3;
    /**
     *已发送
     */
    public static int SEND_SUCCESS = 4;
}
