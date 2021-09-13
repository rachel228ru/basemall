package com.medusa.gruul.account.api.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author whh
 * @description 用户积分加减dto
 * @data: 2019/12/11
 */
@Data
public class IntegraChangelDto implements Serializable {

    private static final long serialVersionUID = 4210079462295980606L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 操作类型  1-增加积分  2-减去积分
     */
    private Integer option;

    /**
     * 操作值
     */
    private BigDecimal value;

    /**
     * 租户id
     */
    private String tenantId;

}
