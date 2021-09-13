package com.medusa.gruul.common.core.param;

import com.medusa.gruul.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 查询参数
 * @Author: alan
 * @Date: 2019/8/29 21:44
 */
@Data
@ApiModel("查询参数对象")
public abstract class QueryParam implements Serializable {
	private static final long serialVersionUID = -3263921252635611410L;

	@ApiModelProperty(value = "页码,默认为1")
	private Integer current = CommonConstants.DEFAULT_PAGE_INDEX;
	@ApiModelProperty(value = "页大小,默认为10")
	private Integer size = CommonConstants.DEFAULT_PAGE_SIZE;
	@ApiModelProperty(value = "搜索字符串")
	private String keyword;

	public void setCurrent(Integer current) {
		if (current == null || current <= 0) {
			this.current = CommonConstants.DEFAULT_PAGE_INDEX;
		} else {
			this.current = current;
		}
	}

	public void setSize(Integer size) {
		if (size == null || size <= 0) {
			this.size = CommonConstants.DEFAULT_PAGE_SIZE;
		} else {
			this.size = size;
		}
	}


}