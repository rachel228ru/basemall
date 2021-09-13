package com.medusa.gruul.shops.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShopsMenuConfigParam 实体", description = "店铺合伙人菜单属性配置 param")
public class ShopsMenuConfigParam extends QueryParam {


    @ApiModelProperty("id")
    private Long id;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 组件属性 json串
     */
    @ApiModelProperty(value = "组件属性 json串")
    private String properties;


    /**
     * 逻辑删除标识  0正常 1已删除
     */
    @ApiModelProperty(value = "逻辑删除标识  0正常 1已删除")
    private String isDeleted;


    /**
     * 归属关联Id
     */
    @ApiModelProperty(value = "归属关联Id")
    private String tenantId;


    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private String operatorId;


    /**
     * 操作人name
     */
    @ApiModelProperty(value = "操作人name")
    private String operatorName;

}
