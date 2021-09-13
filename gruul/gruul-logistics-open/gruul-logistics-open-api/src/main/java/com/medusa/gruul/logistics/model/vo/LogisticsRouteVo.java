 package com.medusa.gruul.logistics.model.vo;

import lombok.Data;

/**
 * Copyright(C) 2020-05-16 09:04 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description:
 * @author: wangpeng
 * @date: 2020-05-16 09:04
 **/
@Data
public class LogisticsRouteVo {
    /**
     * 文本内容
     * */
    private String context;
    /**
     * 时间
     * */
    private String time;
    /**
     * 快递点
     * */
    private String address;

}
