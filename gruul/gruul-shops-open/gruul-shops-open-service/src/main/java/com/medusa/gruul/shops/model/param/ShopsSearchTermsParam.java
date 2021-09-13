package com.medusa.gruul.shops.model.param;

import com.medusa.gruul.common.core.param.QueryParam;
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
@ApiModel(value = "ShopsSearchTermsParam 实体", description = "ShopsSearchTermsParam 实体")
public class ShopsSearchTermsParam extends QueryParam {


    @ApiModelProperty("id")
    private Long id;


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
     * 是否显示 0否 1是
     */
    @ApiModelProperty(value = " 是否显示 0否 1是")
    private String isShow;


    /**
     * 逻辑删除标识  0正常 1已删除
     */
    @ApiModelProperty(value = "逻辑删除标识  0正常 1已删除")
    private String isDeleted;

}
