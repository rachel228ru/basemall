package com.medusa.gruul.shops.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@Data
@ApiModel(value = "热门搜索词汇 vo", description = "热门搜索词汇 vo")
public class ShopsSearchTermsVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;


    /**
     * 词语 多个以英文逗号分隔
     */
    @ApiModelProperty(value = "热搜词语 多个以英文逗号分隔")
    private String terms;


    /**
     * 默认 搜索词语
     */
    @ApiModelProperty(value = "默认 搜索词语")
    private String defTerms;


    /**
     *  是否显示 0否 1是
     */
    @ApiModelProperty(value = " 是否显示 0否 1是")
    private String isShow;


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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
