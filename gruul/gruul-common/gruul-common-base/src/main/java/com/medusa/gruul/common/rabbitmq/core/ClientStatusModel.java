package com.medusa.gruul.common.rabbitmq.core;

import java.sql.Timestamp;

/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public class ClientStatusModel {

    private String type;
    private String host;
    private String status;
    private int random = 50;
    private Timestamp lastModifyTime;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Timestamp lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
