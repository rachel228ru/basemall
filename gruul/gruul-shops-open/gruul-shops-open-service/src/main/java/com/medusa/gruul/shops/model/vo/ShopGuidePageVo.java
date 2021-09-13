package com.medusa.gruul.shops.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 店铺引导页vo
 * @Author: xiaoq
 * @Date : 2020/10/15 16:24
 */
@Data
@ApiModel(value = "ShopGuidePageVo 实体", description = "店铺引导页 vo")
public class ShopGuidePageVo {

	/**
	 * id
	 */
	private Long id;

	@ApiModelProperty(value = "网址")
	private String url ;

	@ApiModelProperty(value = "路径")
	private  String path;

	@ApiModelProperty(value = "是否为默认 0为默认 1为自定义")
	private Integer defaulted;

	@ApiModelProperty(value = "店铺id")
	private String shopId;

	@ApiModelProperty("租户ID")
	private String tenantId;


}
