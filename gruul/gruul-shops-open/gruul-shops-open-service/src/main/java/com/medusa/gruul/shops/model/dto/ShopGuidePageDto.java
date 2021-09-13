package com.medusa.gruul.shops.model.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.medusa.gruul.shops.api.entity.ShopGuidePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 商家自定义引导页dto
 * @Author: xiaoq
 * @Date : 2020/10/16 9:57
 */
@Data
@ApiModel(value = "商家自定义引导页dto")
public class ShopGuidePageDto {

	@ApiModelProperty("id")
	private Long id;
	@ApiModelProperty(value = "网址")
	private String url;

	@ApiModelProperty(value = "路径")
	private String path;

    @ApiModelProperty(value = "path名称 用于比较")
    private String link;

	@ApiModelProperty("跳转的小程序id")
	private String appId;

	@ApiModelProperty(value = " 0为path 1为app_id")
	private Integer type;


	public ShopGuidePage coverShopGuidePage() {
		ShopGuidePage shopGuidePage = new ShopGuidePage();
		BeanUtil.copyProperties(this, shopGuidePage);
		return shopGuidePage;
	}

}
