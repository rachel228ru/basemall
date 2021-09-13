package com.medusa.gruul.common.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RedisTestVo implements Serializable{
    private Long id;
    private Double ps;
    private String name;
    private Date time;

    public void clear(){
        this.id=null;
        this.ps=null;
        this.name=null;
        this.time=null;
    }
}
