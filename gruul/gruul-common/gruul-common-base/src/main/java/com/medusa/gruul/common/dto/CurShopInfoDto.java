package com.medusa.gruul.common.dto;

import lombok.Data;

/**
 * @author whh
 * @description 当前线程环境中的租户id的店铺信息
 * @data: 2021/1/27
 */
@Data
public class CurShopInfoDto {

    /**
     * 店铺拥有者的平台账号id
     */
    private Long platfromUserId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 绑定的小程序表id
     * 可能存在为空，未授权小程序
     */
    private Long bindMiniId;

    /**
     * 绑定的公众号表id
     * 可能存在为空，未授权公众号
     */
    private Long bindMpId;

    /**
     * 店铺当前使用套餐等级
     */
    private Integer level;

    /**
     * 店铺当前使用的套餐名称
     */
    private String packageName;


    /**
     * 店铺当前使用的模板版本
     * 可能存在为空，新店铺未上传小程序没有
     */
    private String templateVersion;

}
