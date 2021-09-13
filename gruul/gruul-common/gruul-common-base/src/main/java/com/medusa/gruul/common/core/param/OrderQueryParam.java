package com.medusa.gruul.common.core.param;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 可排序查询参数对象
 * @Author: alan
 * @Date: 2019/8/29 21:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("可排序查询参数对象")
public abstract class OrderQueryParam extends QueryParam {
	private static final long serialVersionUID = 57714391204790143L;

	@ApiModelProperty(value = "排序")
	private List<OrderItem> orders;

	public void defaultOrder(OrderItem orderItem) {
		this.defaultOrders(Arrays.asList(orderItem));
	}

	public void defaultOrders(List<OrderItem> orderItems) {
		if (CollectionUtils.isEmpty(orderItems)) {
			return;
		}
		this.orders = orderItems;
	}

}
