package com.medusa.gruul.account.api.model;

import com.medusa.gruul.account.api.entity.MiniAccount;
import com.medusa.gruul.account.api.entity.MiniAccountAddress;
import com.medusa.gruul.account.api.entity.MiniAccountExtends;
import com.medusa.gruul.account.api.entity.MiniAccountOauths;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author whh
 * @description
 * @data: 2019/11/29
 */
@Data
public class AccountInfoDto {

    @ApiModelProperty("用户地址数组,自行判空")
    private List<MiniAccountAddress> miniAccountAddress;

    @ApiModelProperty("用户扩展数据")
    private MiniAccountExtends miniAccountExtends;

    @ApiModelProperty("用户授权信息")
    private MiniAccountOauths miniAccountOauths;

    @ApiModelProperty("用户基础数据")
    private MiniAccount miniAccountunt;

    @ApiModelProperty("黑名单限制类型数组,查询扩展数据时返回,可能为null,自行判断")
    private List<Integer> restrictTypes;



}
