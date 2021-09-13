package com.medusa.gruul.afs.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * The type Return address vo.
 *
 * @author alan
 */
@Data
@ApiModel(value = "退货地址信息")
public class ReturnAddressVo {

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String name;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    @TableField("phone")
    private String phone;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
}
